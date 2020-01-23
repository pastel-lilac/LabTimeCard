package com.batch.labtimecard.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.batch.labtimecard.model.Member
import com.batch.labtimecard.model.MemberData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MemberListViewModel(application: Application): AndroidViewModel(application) {

    val TAG = "ORENOTAG"

    val members = MutableLiveData<List<MemberData>>()
    private lateinit var database: FirebaseDatabase
    private val context = getApplication<Application>().applicationContext

    fun fetchFromRemote() {
        val memberDataList: MutableList<MemberData> = mutableListOf()
        database = FirebaseDatabase.getInstance()
        val ref = database.getReference("members")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                memberDataList.clear()
                dataSnapshot.children.forEach { ds ->
                    val member = ds.getValue(Member::class.java)
                    Log.d(TAG, member.toString())
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

    fun login(item: MemberData) {
        val date = Date(System.currentTimeMillis())
        val today = SimpleDateFormat("yyyy-MM-dd").format(date)
        val time = SimpleDateFormat("HH:mm:ss").format(date)
        val name = item.member?.name
        val timeMap: MutableMap<String, Any> = HashMap()
        val refLog = database.getReference("logs").child(item.key.toString()).child(today)
        val refAct = database.getReference("members").child(item.key.toString())

        val isActive = item.member?.active ?: true
        val actMap: MutableMap<String, Any> = HashMap()
        if (isActive) {
            timeMap["logoutTime"] = time
            actMap["active"] = false
            refLog.updateChildren(timeMap)
            refAct.updateChildren(actMap)
            Toast.makeText(context, "ログアウトしました\n名前${name} 時刻${time}", Toast.LENGTH_SHORT).show()

        } else {
            timeMap["loginTime"] = time
            timeMap["logoutTime"] = ""
            actMap["active"] = true
            refLog.setValue(timeMap)
            refAct.updateChildren(actMap)
            Toast.makeText(context, "ログインしました\n名前${name} 時刻${time}", Toast.LENGTH_SHORT).show()
        }
    }
}