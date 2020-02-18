package com.batch.labtimecard.data

import com.batch.labtimecard.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.*


interface TimeCardRepository {
    fun fetchMembers(): Flow<List<MemberData>>
    suspend fun fetchMembersOnce(): List<MemberData>
    suspend fun login(memberKey: String)
    suspend fun logout(memberKey: String)
    suspend fun updateLog(memberKey: String, isLogin: Boolean)
    suspend fun fetchLog(memberKey: String, month: Date): List<LoginLog>
    suspend fun registerMember(member: Member)
    suspend fun removeMember(memberKey: String)
    suspend fun updateMemberProfile(memberKey: String, member: Member)
    suspend fun getGroupMembers(groupId: String): GroupMembers
    suspend fun getMemberProfile(slackId: String): MemberProfile
    suspend fun removeMembers()
}
