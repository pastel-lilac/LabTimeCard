package com.batch.labtimecard.member.usecase

import com.batch.labtimecard.data.model.MemberData
import kotlinx.coroutines.CoroutineScope

interface MemberUseCase {
    suspend fun updateYear(scope: CoroutineScope)
    suspend fun refreshMembers(scope: CoroutineScope, members: List<MemberData>)
}