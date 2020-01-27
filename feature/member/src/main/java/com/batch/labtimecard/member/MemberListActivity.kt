package com.batch.labtimecard.member

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.common.navigator.Navigator

import com.batch.labtimecard.data.model.MemberData

import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_member_list.*
import org.koin.android.ext.android.inject

class MemberListActivity : AppCompatActivity(),
    MemberListController.ClickListener {

    private val navigator: Navigator by inject()

    private lateinit var database: FirebaseDatabase
    private val controller by lazy {
        MemberListController(
            this
        )
    }
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
            val manager = GridLayoutManager(applicationContext, 4).apply {
                spanSizeLookup = controller.spanSizeLookup
                recycleChildrenOnDetach = true
            }
            layoutManager = manager
        }
//        button_person_add.visibility = View.GONE
        button_person_add.setOnClickListener {
            navigator.run { navigateToRegist() }
        }
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
    }

    companion object {
        fun createIntent(activity: Activity) = Intent(activity, MemberListActivity::class.java)
    }
}

