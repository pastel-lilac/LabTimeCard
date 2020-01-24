package com.batch.labtimecard.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.R
import com.batch.labtimecard.model.MemberData
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_member_list.*

class MemberListActivity : AppCompatActivity(), MemberListController.ClickListener {

    private lateinit var database: FirebaseDatabase
    private val controller by lazy { MemberListController(this) }
    private lateinit var viewModel: MemberListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)
        viewModel = ViewModelProviders.of(this).get(MemberListViewModel::class.java)
        observeMembers()
        database = FirebaseDatabase.getInstance()
        viewModel.fetchFromRemote()
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

    private fun observeMembers() {
        viewModel.members.observe(this, Observer {
            controller.setData(it)
        })
    }

    override fun itemClickListener(item: MemberData) {
        viewModel.loginLogout(item)
    }

    override fun buttonClickListener(item: MemberData) {
        Log.d("ORENO", item.toString())
        Toast.makeText(applicationContext, "more button tap\n${item.member}", Toast.LENGTH_SHORT).show()
    }
}
