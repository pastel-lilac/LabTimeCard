package com.batch.labtimecard.member

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.common.navigator.Navigator
import com.batch.labtimecard.data.model.MemberData
import kotlinx.android.synthetic.main.activity_member_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MemberListActivity : AppCompatActivity() {

    private val navigator: Navigator by inject()

    private val listener = object : MemberListController.ClickListener {
        override fun itemClickListener(item: MemberData) {
            viewModel.updateLoginState(item)
        }

        override fun buttonClickListener(item: MemberData) {
        }
    }
    private val controller by lazy { MemberListController(listener) }

    private val viewModel: MemberListViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)
        observeMembers()
        observeIsLoggedIn()
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


    private fun observeIsLoggedIn() {
        viewModel.isLoggedIn.observe(this, Observer {
            val name = it.first
            val message = if (it.second) "${name}がログインしました" else "${name}ログアウトしました"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        })
    }


    companion object {
        fun createIntent(activity: Activity) = Intent(activity, MemberListActivity::class.java)
    }
}

