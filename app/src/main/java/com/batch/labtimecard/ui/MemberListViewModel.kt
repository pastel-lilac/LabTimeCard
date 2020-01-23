package com.batch.labtimecard.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batch.labtimecard.R
import com.batch.labtimecard.model.Member
import com.batch.labtimecard.model.MemberData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MemberListViewModel: ViewModel() {

    val TAG = "ORENOTAG"

    val members = MutableLiveData<List<MemberData>>()
    private lateinit var database: FirebaseDatabase

    fun fetchFromRemote() {
        val memberDataList: MutableList<MemberData> = mutableListOf()
        database = FirebaseDatabase.getInstance()
        val ref = database.getReference("members")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                memberDataList.clear()
                dataSnapshot.children.forEach { ds ->
                    val member = ds.getValue(Member::class.java)
                    if (member != null) {
                        Log.d(TAG, member.toString())
                        memberDataList.add(MemberData(ds.key, member))
                    }
                }
                members.value = memberDataList
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}