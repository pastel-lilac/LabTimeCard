package com.batch.labtimecard.data

import com.batch.labtimecard.data.model.Member
import com.batch.labtimecard.data.model.MemberData
import kotlinx.coroutines.flow.Flow


interface TimeCardRepository {
    fun fetchMembers(): Flow<List<MemberData>>
    suspend fun login(memberKey: String)
    suspend fun logout(memberKey: String)
    suspend fun updateLog(memberKey: String, isLogin: Boolean)
    suspend fun registerMember(member: Member)
}
