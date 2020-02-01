package com.batch.labtimecard.data

import com.batch.labtimecard.data.model.Member
import com.batch.labtimecard.data.model.MemberData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TimeCardRepositoryImpl : TimeCardRepository {

    private val database = FirebaseDatabase.getInstance()

    @ExperimentalCoroutinesApi
    override fun fetchMembers() = channelFlow<List<MemberData>> {
        val dbRef = database.getReference(DatabaseKey.MEMBER)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val members = snapshot.children.mapNotNull { ds ->
                    ds.getValue(Member::class.java)?.let { member ->
                        MemberData(ds.key, member)
                    }
                }
                channel.offer(members)
            }

            override fun onCancelled(e: DatabaseError) {
                Timber.e(e.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    override suspend fun login(memberKey: String) {
        val ref = database.getReference(DatabaseKey.MEMBER).child(memberKey)
        ref.updateChildren(mapOf("active" to true))
            .addOnFailureListener {
                throw it
            }.await()
    }

    override suspend fun logout(memberKey: String) {
        val ref = database.getReference(DatabaseKey.MEMBER).child(memberKey)
        ref.updateChildren(mapOf("active" to false))
            .addOnFailureListener {
                throw it
            }.await()

    }

    override suspend fun updateLog(memberKey: String, isLogin: Boolean) {
        val date = Calendar.getInstance().time
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN).format(date)
        val now = SimpleDateFormat("HH:mm:ss", Locale.JAPAN).format(date)
        val ref = database
            .getReference(DatabaseKey.LOGS)
            .child(memberKey)
            .child(today)
        if (isLogin) {
            val data = mapOf("loginTime" to now, "logoutTime" to "")
            ref.setValue(data).addOnFailureListener {
                throw it
            }.await()
        } else {
            val data = mapOf("logoutTime" to now)
            ref.updateChildren(data).addOnFailureListener {
                throw it
            }.await()
        }
    }
}