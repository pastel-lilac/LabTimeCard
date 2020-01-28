package com.batch.labtimecard.log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batch.labtimecard.data.model.LogTime
import com.batch.labtimecard.data.model.LoginLog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LogViewModel : ViewModel() {
    val logs = MutableLiveData<List<LoginLog>>()
    private lateinit var database: FirebaseDatabase

    fun fetchFromRemote(memberKey: String) {
        val logDataList: MutableList<LoginLog> = mutableListOf()
        database = FirebaseDatabase.getInstance()
        val ref = database.getReference("logs").child(memberKey)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                logDataList.clear()
                dataSnapshot.children.forEach { ds ->
                    val time = ds.getValue(LogTime::class.java)
                    if (time != null) {
                        logDataList.add(LoginLog(ds.key, time))
                    }
                }
                logs.value = logDataList
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}