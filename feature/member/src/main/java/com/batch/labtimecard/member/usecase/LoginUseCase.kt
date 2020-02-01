package com.batch.labtimecard.member.usecase

import com.batch.labtimecard.data.model.MemberData

interface LoginUseCase {
    suspend fun updateLoginState(memberData: MemberData): Boolean
}