package com.batch.labtimecard.ui

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
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormat.format(date)
        val ref  = database.getReference("logs").child(item.name.toString()).child(today)
        val insertDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val loginTime = insertDateFormat.format(date)
        val timeMap = kotlin.collections.HashMap<String, String>()
        timeMap.set("loginTime", loginTime)
        ref.setValue(timeMap)
        Toast.makeText(applicationContext, "${item.name}+${loginTime}", Toast.LENGTH_SHORT).show()
    }
}
