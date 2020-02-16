package com.batch.labtimecard.member.usecase

import com.batch.labtimecard.data.model.Member
import com.batch.labtimecard.data.model.MemberData
import kotlinx.coroutines.CoroutineScope

interface MemberUseCase {
    suspend fun getAllMemberProfile(scope: CoroutineScope): List<Member>
    suspend fun registerMembers(members: List<Member>)
    suspend fun refreshMembers(scope: CoroutineScope, members: List<MemberData>)
}