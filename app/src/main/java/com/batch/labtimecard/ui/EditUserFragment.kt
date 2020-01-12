package com.batch.labtimecard.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.batch.labtimecard.R
import com.batch.labtimecard.model.Member
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_edit_user.*

class EditUserFragment : Fragment() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        // ラジオボタンが変更されたときとか：https://techbooster.org/android/ui/9640/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_register.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = name_edit_text.text.toString()
        val checkedId = affiliation_radio_group.checkedRadioButtonId
        val affiliationLabName = when (checkedId) {
            -1 -> null
            else -> view?.findViewById<RadioButton>(checkedId)?.text
        }
        // members配下にmemberデータの挿入
        val ref = database.getReference("members").push()
        val member = Member(name = name, affiliationLabName = affiliationLabName.toString())
        ref.setValue(member)

        if (TextUtils.isEmpty(name) || affiliationLabName == null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("確認")
            builder.setMessage("終了してもよろしいですか？")
                .setPositiveButton("OK") { _, _ ->
                    fragmentManager?.popBackStack()
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
            builder.show()
        } else {
            fragmentManager?.popBackStack()
            Toast.makeText(requireContext(), "${affiliationLabName}の${name}で保存しました", Toast.LENGTH_SHORT).show()
        }
    }

}
