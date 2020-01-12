package com.batch.labtimecard.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.R
import com.batch.labtimecard.model.Member
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_member_list.*
import java.text.SimpleDateFormat
import java.util.*


class MemberListActivity : AppCompatActivity(), MemberListController.ClickListener {

    private val TAG = "ORENOTAG"


    private lateinit var database: FirebaseDatabase
    private val controller by lazy { MemberListController(this) }
    val memberList: MutableList<Member> = mutableListOf()

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
            openEditPersonScreen()
        }
    }

    private fun openEditPersonScreen() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(
            R.id.fragment_container,
            EditUserFragment()
        )
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun readDatabase() {
        val ref = database.getReference("members")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                memberList.clear()
                dataSnapshot.children.forEach { ds ->
                    Log.d(TAG, ds.toString())
                    val member = ds.getValue(Member::class.java)
                    if (member != null) {
                        memberList.add(member)
                    }
                }
                controller.setData(memberList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun itemClickListenter(item: Member) {
        val pref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormat.format(date)
        val insertDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val time = insertDateFormat.format(date)
        val timeMap = kotlin.collections.HashMap<String, String>()
        timeMap.set("time", time)
        Toast.makeText(applicationContext, pref.getBoolean("${item.name}isExisting", false).toString(), Toast.LENGTH_SHORT).show()
        if (pref.getBoolean("${item.name}isExisting", false)) {
            val ref  = database.getReference("logs").child(item.name.toString()).child(today).child("logout")
            Toast.makeText(applicationContext, "Logout! ${item.name}+${time}", Toast.LENGTH_SHORT).show()
            pref.edit().apply {
                putBoolean("${item.name}isExisting", false)
                commit()
            }
            ref.setValue(timeMap)
        } else {
            val ref  = database.getReference("logs").child(item.name.toString()).child(today).child("login")
            Toast.makeText(applicationContext, "Login! ${item.name}+${time}", Toast.LENGTH_SHORT).show()
            pref.edit().apply {
                putBoolean("${item.name}isExisting", true)
                commit()
            }
            ref.setValue(timeMap)

        }
    }
}
