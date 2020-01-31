package com.batch.labtimecard.member

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.common.navigator.Navigator

import com.batch.labtimecard.data.model.MemberData

import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_member_list.*
import org.koin.android.ext.android.inject
import java.security.acl.Owner

class MemberListActivity : AppCompatActivity(),
    MemberListController.ClickListener {

    private val navigator: Navigator by inject()

    private lateinit var database: FirebaseDatabase
    private val controller by lazy {
        MemberListController(
            this
        )
    }
    private val viewModel: MemberListViewModel by lazy {
        ViewModelProvider(this).get(MemberListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)
//        viewModel = ViewModelProviders.of(this).get(MemberListViewModel::class.java)
        observeMembers()
        observeIsLogedIn()
        database = FirebaseDatabase.getInstance()
        viewModel.fetchFromRemote()
        member_list_recycler_view.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = controller.adapter
            val manager = GridLayoutManager(applicationContext, 5).apply {
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

    private fun observeIsLogedIn() {
        viewModel.isLoggedIn.observe(this, Observer {
            val name = it.first
            val message = if (it.second) "${name}がログアウトしました" else "${name}ログインしました"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
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

