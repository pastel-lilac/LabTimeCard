package com.batch.labtimecard.member

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batch.labtimecard.data.api.Group
import com.batch.labtimecard.data.model.MemberData
import com.batch.labtimecard.member.MemberListController.Companion.SPAN_COUNT
import com.batch.labtimecard.member.databinding.ActivityMemberListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MemberListActivity : AppCompatActivity() {

    private val listener = object : MemberListController.ClickListener {
        override fun itemClickListener(item: MemberData) {
            viewModel.updateLoginState(item)
        }

        override fun buttonClickListener(item: MemberData) {
        }
    }
    private val controller by lazy { MemberListController(listener) }

    private val viewModel: MemberListViewModel by viewModel()

    private lateinit var binding: ActivityMemberListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_list)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        observeMembers()
        observeIsLoggedIn()
        observeIsLoading()
        observeUpdatingMembers()
        viewModel.fetchFromRemote()
        binding.memberListRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = controller.adapter
            val manager = GridLayoutManager(applicationContext, SPAN_COUNT).apply {
                spanSizeLookup = controller.spanSizeLookup
                recycleChildrenOnDetach = true
            }
            layoutManager = manager
        }
        binding.buttonPersonAdd.setOnClickListener {
            AlertDialog.Builder(this, R.style.common_MyAlertDialogStyle).apply {
                setTitle("確認")
                setMessage("メンバーを更新します")
                setPositiveButton("OK") { _, _ ->
                    viewModel.isLoading.value = true
                    viewModel.updateAllMember()
                }
                setNegativeButton("Cancel") { _, _ ->
                }
            }.show()
        }
        binding.refresh.setOnRefreshListener {
            viewModel.isRefreshing.value = true
            viewModel.refresh()
        }
    }

    private fun observeMembers() {
        viewModel.members.observe(this, Observer {
            val comparator = compareBy<MemberData> { data ->
                val gradeString = data.member?.grade ?: return@compareBy null
                val group = Group.nameToEnum(gradeString)
                group.orderNumber
            }.thenByDescending { data ->
                data.member?.active
            }.thenByDescending { data -> data.key }
            val ordered = it.sortedWith(comparator)
            controller.setData(ordered)
        })
    }

    private fun observeUpdatingMembers() {
        viewModel.updatingMembers.observe(this, Observer {
            if (it) return@Observer
            Toast.makeText(this, "メンバーを更新しました", Toast.LENGTH_SHORT).show()
        })
    }


    private fun observeIsLoggedIn() {
        viewModel.isLoggedIn.observe(this, Observer {
            val name = it.first
            val message = if (it.second) "${name}がログインしました" else "${name}がログアウトしました"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun observeIsLoading() {
        viewModel.isLoading.observe(this, Observer {
            binding.progressBar.isVisible = it
        })
    }

    companion object {
        fun createIntent(activity: Activity) = Intent(activity, MemberListActivity::class.java)
    }
}

