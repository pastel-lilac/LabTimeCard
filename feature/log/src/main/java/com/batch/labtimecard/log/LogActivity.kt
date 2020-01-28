package com.batch.labtimecard.log

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_log.*

class LogActivity : AppCompatActivity() {

    private lateinit var viewModel: LogViewModel
    private val controller by lazy { LogListController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        viewModel = ViewModelProviders.of(this).get(LogViewModel::class.java)
        observeLogs()
        val memberKey = intent.getStringExtra(MEMBER) ?: ""
        viewModel.fetchFromRemote(memberKey)
        log_recycler_view.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = controller.adapter
        }
    }

    private fun observeLogs() {
        viewModel.logs.observe(this, Observer {
            controller.setData(it)
        })
    }

    companion object {
        private const val MEMBER = "memberKey"

        fun createIntent(activity: Activity, member: String?) =
            Intent(activity, LogActivity::class.java).apply {
                putExtra(MEMBER, member)
            }

    }
}
