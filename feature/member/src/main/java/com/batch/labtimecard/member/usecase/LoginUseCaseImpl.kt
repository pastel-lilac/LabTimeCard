package com.batch.labtimecard.member.usecase

import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.model.MemberData

class LoginUseCaseImpl(
    private val timeCardRepository: TimeCardRepository
) : LoginUseCase {
    override suspend fun updateLoginState(memberData: MemberData): Boolean {
        val isActive =
            memberData.member?.active ?: throw IllegalArgumentException("isActive must not be null")
        val key = memberData.key ?: throw IllegalArgumentException("memberKey must not be null")
        if (!isActive) {
            timeCardRepository.login(key)
            timeCardRepository.updateLog(key, true)
            return true
        }
        timeCardRepository.logout(key)
        timeCardRepository.updateLog(key, false)
        return false
    }
}