package com.batch.labtimecard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
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
            -1 -> "未所属"
            else -> view?.findViewById<RadioButton>(checkedId)?.text
        }
        val ref = database.getReference("members").push()
        val member = Member(name = name, affiliation_lab_name = affiliationLabName.toString())
        ref.setValue(member)
        fragmentManager?.popBackStack()
        Toast.makeText(requireContext(), "${affiliationLabName}の${name}で保存しました", Toast.LENGTH_SHORT).show()
    }
}
