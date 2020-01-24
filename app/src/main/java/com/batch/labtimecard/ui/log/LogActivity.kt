package com.batch.labtimecard.ui.log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.R
import kotlinx.android.synthetic.main.activity_log.*

class LogActivity : AppCompatActivity() {

    private lateinit var viewModel: LogViewModel
    private val controller by lazy { LogListController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        viewModel = ViewModelProviders.of(this).get(LogViewModel::class.java)
        observeLogs()
        val intent = getIntent()
        val memberKey = intent.getStringExtra("memberKey") ?: ""
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
}
