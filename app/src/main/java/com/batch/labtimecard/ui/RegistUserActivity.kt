package com.batch.labtimecard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.batch.labtimecard.R
import com.batch.labtimecard.model.Member
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_regist_user.*

class RegistUserActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

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
        val member = Member(name = name, affiliationLabName = affiliationLabName.toString(), active = false)
        ref.setValue(member)
        val intent = Intent(this, MemberListActivity::class.java)


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
            Toast.makeText(applicationContext, "${affiliationLabName}の${name}で保存しました", Toast.LENGTH_SHORT).show()
        }
        startActivity(intent)
    }
}
