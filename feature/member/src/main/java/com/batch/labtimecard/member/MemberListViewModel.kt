package com.batch.labtimecard.member

import androidx.lifecycle.*
import com.batch.labtimecard.data.model.Member
import com.batch.labtimecard.data.model.MemberData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MemberListViewModel() : ViewModel() {

    val members = MutableLiveData<List<MemberData>>()
    private val _isLoggedIn = MutableLiveData<Pair<String?, Boolean>>()
    val isLoggedIn: LiveData<Pair<String?, Boolean>> = _isLoggedIn
    private lateinit var database: FirebaseDatabase

    fun fetchFromRemote() {
        val memberDataList: MutableList<MemberData> = mutableListOf()
        database = FirebaseDatabase.getInstance()
        val ref = database.getReference("members")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                memberDataList.clear()
                dataSnapshot.children.forEach { ds ->
                    val member = ds.getValue(Member::class.java)
                    if (member != null) {
                        memberDataList.add(MemberData(ds.key, member))
                    }
                }
                members.value = memberDataList
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun loginLogout(item: MemberData) {
        val date = Date(System.currentTimeMillis())
        val today = SimpleDateFormat("yyyy-MM-dd").format(date)
        val time = SimpleDateFormat("HH:mm:ss").format(date)
        val name = item.member?.name
        val timeMap: MutableMap<String, Any> = HashMap()
        val refLog = database.getReference("logs").child(item.key.toString()).child(today)
        val refAct = database.getReference("members").child(item.key.toString())
        val isActive = item.member?.active ?: false
        val actMap: MutableMap<String, Any> = HashMap()
//        _isLogedIn.value = _isLogedIn.value?.let { !it } ?: false // テクい書き方
        if (isActive) {
            _isLoggedIn.value = Pair(name, false)
            timeMap["logoutTime"] = time
            actMap["active"] = isLoggedIn.value?.second ?: false
            refLog.updateChildren(timeMap)
        } else {
            _isLoggedIn.value = Pair(name, true)
            timeMap["loginTime"] = time
            timeMap["logoutTime"] = ""
            actMap["active"] = isLoggedIn.value?.second ?: true
            refLog.setValue(timeMap)
        }
        refAct.updateChildren(actMap)
    }
}