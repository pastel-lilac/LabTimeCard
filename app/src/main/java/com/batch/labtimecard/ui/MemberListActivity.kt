package com.batch.labtimecard.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.R
import com.batch.labtimecard.model.Member
import com.batch.labtimecard.model.MemberData
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_member_list.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MemberListActivity : AppCompatActivity(), MemberListController.ClickListener {

    private val TAG = "ORENOTAG"


    private lateinit var database: FirebaseDatabase
    private val controller by lazy { MemberListController(this) }
    val memberDataList: MutableList<MemberData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)
        database = FirebaseDatabase.getInstance()
        readDatabase()

        member_list_recycler_view.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = controller.adapter
            val manager= GridLayoutManager(applicationContext, 4).apply {
                spanSizeLookup = controller.spanSizeLookup
                recycleChildrenOnDetach = true
            }
            layoutManager = manager
        }
        button_person_add.setOnClickListener {
            openRegistUserActivity()
        }
    }

    private fun openRegistUserActivity() {
        val intent = Intent(this, RegistUserActivity::class.java)
        startActivity(intent)
    }

    private fun readDatabase() {
        val ref = database.getReference("members")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                memberDataList.clear()
                dataSnapshot.children.forEach { ds ->
                    Log.d(TAG, ds.toString())
                    val member = ds.getValue(Member::class.java)
                    if (member != null) {
                        memberDataList.add(MemberData(ds.key, member))
                    }
                }
                controller.setData(memberDataList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun itemClickListener(item: MemberData) {
        val pref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormat.format(date)
        val insertDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val time = insertDateFormat.format(date)
        val name = item.member.name
        val timeMap: MutableMap<String, Any> = HashMap()
        val ref  = database.getReference("logs").child(name.toString()).child(today)
        if (pref.getBoolean("${name}isExisting", false)) { // 選択されたメンバーがログイン中のとき
            // databaseに保存する子のキー名をlogoutにする
            timeMap["logoutTime"] = time
            Toast.makeText(applicationContext, "ログアウトしました\n名前${name} 時刻${time}", Toast.LENGTH_SHORT).show()
            pref.edit().apply {
                putBoolean("${name}isExisting", false)
                commit()
            }
            // ログアウトした時間をdatabaseに保存
            ref.updateChildren(timeMap)
        } else { // 選択されたメンバーがログイン中じゃないとき
            // databaseに保存する子のキー名をloginにする
            timeMap["loginTime"] = time
            timeMap["logoutTime"] = ""
            Toast.makeText(applicationContext, "ログインしました\n名前${name} 時刻${time}", Toast.LENGTH_SHORT).show()
            pref.edit().apply {
                putBoolean("${name}isExisting", true)
                commit()
            }
            // ログインした時間をdatabaseに保存
            ref.setValue(timeMap)
        }
    }
}
