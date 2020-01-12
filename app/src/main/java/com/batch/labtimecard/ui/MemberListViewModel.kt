package com.batch.labtimecard.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batch.labtimecard.model.Member
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MemberListViewModel: ViewModel() {
    val memberList: MutableList<Member> = mutableListOf()
    private lateinit var database: FirebaseDatabase


    fun fetchRemote(): MutableList<Member> {
        val ref = database.getReference("members")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                memberList.clear()
                dataSnapshot.children.forEach { ds ->
                    val member = ds.getValue(Member::class.java)
                    if (member != null) {
                        memberList.add(member)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return memberList
    }
}