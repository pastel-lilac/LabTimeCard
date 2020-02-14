package com.github.pastel_lilac.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.batch.labtimecard.common.navigator.Navigator
import com.batch.labtimecard.data.model.Member
import com.github.pastel_lilac.register.databinding.ActivityRegisterUserBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding

    private val navigator: Navigator by inject()
    private val viewModel: RegisterUserViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_user)
        binding.viewModel = viewModel
        binding.apply {
            lifecycleOwner = this@RegisterUserActivity
            buttonRegister.setOnClickListener {
                register()
            }
        }
        observeRegisterResult()
    }


    private fun register() {
        val name = binding.nameEditText.text.toString()
        val labId = binding.affiliationRadioGroup.checkedRadioButtonId
        val labName = findViewById<RadioButton>(labId).text.toString()
        // members配下にmemberデータの挿入
        val member =
            Member(
                name = name,
                affiliationLabName = labName,
                active = false
            )
        viewModel.registerUser(member)
    }

    override fun onBackPressed() {
        val registerEnabled = viewModel.registerButtonEnabled.value ?: false
        if (registerEnabled) {
            AlertDialog.Builder(applicationContext).apply {
                setTitle("確認")
                setMessage("終了してもよろしいですか？")
                    .setPositiveButton("OK") { _, _ ->
                        finish()
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                    }
            }.show()
            return
        }
        super.onBackPressed()

    }

    private fun observeRegisterResult() {
        viewModel.isRegisterSucceeded.observe(this, Observer {
            val name = viewModel.name.value
            val labId = binding.affiliationRadioGroup.checkedRadioButtonId
            val labName = findViewById<RadioButton>(labId).text.toString()
            if (it) {
                Toast.makeText(
                    applicationContext,
                    "${labName}の${name}で保存しました",
                    Toast.LENGTH_SHORT
                ).show()
                navigator.run { navigateToMember() }
            } else {
                Toast.makeText(
                    applicationContext,
                    "登録失敗！残念...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    companion object {
        fun createIntent(activity: Activity) = Intent(activity, RegisterUserActivity::class.java)
    }
}
