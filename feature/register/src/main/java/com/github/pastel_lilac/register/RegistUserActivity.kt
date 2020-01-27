package com.github.pastel_lilac.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.batch.labtimecard.common.navigator.Navigator
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_regist_user.*
import org.koin.android.ext.android.inject

class RegistUserActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_user)
        database = FirebaseDatabase.getInstance()
        button_register.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = name_edit_text.text.toString()
        val checkedId = affiliation_radio_group.checkedRadioButtonId
        val affiliationLabName = when (checkedId) {
            -1 -> null
            else -> findViewById<RadioButton>(checkedId)?.text
        }
        // members配下にmemberデータの挿入
        val ref = database.getReference("members").push()
        val member =
            com.batch.labtimecard.data.model.Member(
                name = name,
                affiliationLabName = affiliationLabName.toString(),
                active = false
            )
        ref.setValue(member)
        navigator.run { navigateToMember() }


        if (TextUtils.isEmpty(name) || affiliationLabName == null) {
            val builder = AlertDialog.Builder(applicationContext)
            builder.setTitle("確認")
            builder.setMessage("終了してもよろしいですか？")
                .setPositiveButton("OK") { _, _ ->
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
            builder.show()
        } else {
            Toast.makeText(
                applicationContext,
                "${affiliationLabName}の${name}で保存しました",
                Toast.LENGTH_SHORT
            ).show()
        }
        startActivity(intent)
    }

    companion object {
        fun createIntent(activity: Activity) = Intent(activity, RegistUserActivity::class.java)
    }
}
