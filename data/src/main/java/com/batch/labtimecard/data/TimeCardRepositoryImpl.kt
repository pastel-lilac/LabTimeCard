package com.batch.labtimecard.data

import com.batch.labtimecard.data.api.SLACK_TOKEN
import com.batch.labtimecard.data.api.SlackClient
import com.batch.labtimecard.data.model.GroupMembers
import com.batch.labtimecard.data.model.Member
import com.batch.labtimecard.data.model.MemberData
import com.batch.labtimecard.data.model.MemberProfile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TimeCardRepositoryImpl(
    retrofit: Retrofit
) : TimeCardRepository {

    private val database = FirebaseDatabase.getInstance()

    private val client = retrofit.create(SlackClient::class.java)

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

    @ExperimentalCoroutinesApi
    override suspend fun fetchMembersOnce() =
        suspendCancellableCoroutine<List<MemberData>> { cont ->
            val dbRef = database.getReference(DatabaseKey.MEMBER)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val members = snapshot.children.mapNotNull { ds ->
                        ds.getValue(Member::class.java)?.let { member ->
                            MemberData(ds.key, member)
                        }
                    }
                    cont.resume(members)
                    dbRef.removeEventListener(this)
                }

                override fun onCancelled(e: DatabaseError) {
                    cont.resumeWithException(e.toException())
                    dbRef.removeEventListener(this)
                }
            }
            dbRef.addListenerForSingleValueEvent(listener)
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

    override suspend fun registerMember(member: Member) {
        val ref = database.getReference(DatabaseKey.MEMBER).push()
        ref.setValue(member)
            .addOnFailureListener {
                throw it
            }.await()
    }

    override suspend fun removeMember(memberKey: String) {
        val ref = database.getReference(DatabaseKey.MEMBER).child(memberKey)
        ref.removeValue().addOnFailureListener {
            throw it
        }.await()
    }

    override suspend fun updateMemberProfile(memberKey: String, member: Member) {
        val ref = database.getReference(DatabaseKey.MEMBER).child(memberKey)
        ref.setValue(member)
            .addOnFailureListener {
                throw it
            }.await()
    }

    override suspend fun getGroupMembers(groupId: String): GroupMembers {
        return client.getGroupMembers(SLACK_TOKEN, groupId)
    }

    override suspend fun getMemberProfile(slackId: String): MemberProfile {
        return client.getMemberProfile(SLACK_TOKEN, slackId)
    }

    override suspend fun removeMembers() {
        database.getReference(DatabaseKey.MEMBER).removeValue()
    }
}