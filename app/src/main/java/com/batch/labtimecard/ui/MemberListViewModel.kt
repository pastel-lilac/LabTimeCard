package com.batch.labtimecard.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batch.labtimecard.R
import com.batch.labtimecard.model.Member
import com.batch.labtimecard.model.MemberData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
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

    fun login(item: MemberData) {
        val pref = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        val date = Date(System.currentTimeMillis())
        val today = SimpleDateFormat("yyyy-MM-dd").format(date)
        val time = SimpleDateFormat("HH:mm:ss").format(date)
        val name = item.member.name
        val timeMap: MutableMap<String, Any> = HashMap()
        val ref = database.getReference("logs").child(name.toString()).child(today)
        if (pref.getBoolean("${name}isExisting", false)) {
            timeMap["logoutTime"] = time
            Toast.makeText(context, "ログアウトしました\n名前${name} 時刻${time}", Toast.LENGTH_SHORT).show()
            pref.edit().apply {
                putBoolean("${name}isExisting", false)
                commit()
            }
            ref.updateChildren(timeMap)
        } else {
            timeMap["loginTime"] = time
            timeMap["logoutTime"] = ""
            Toast.makeText(context, "ログインしました\n名前${name} 時刻${time}", Toast.LENGTH_SHORT).show()
            pref.edit().apply {
                putBoolean("${name}isExisting", true)
                commit()
            }
            // ログインした時間をdatabaseに保存
            ref.setValue(timeMap)
        }
    }
}