package com.batch.labtimecard.data.api

import com.batch.labtimecard.data.model.GroupMembers
import com.batch.labtimecard.data.model.MemberProfile
import retrofit2.http.GET
import retrofit2.http.Query

interface SlackClient {
    @GET(GROUP_MEMBERS)
    suspend fun getGroupMembers(
        @Query("token") token: String,
        @Query("usergroup") groupId: String
    ): GroupMembers

    @GET(MEMBER_PROFILE)
    suspend fun getMemberProfile(
        @Query("token") token: String,
        @Query("user") userId: String
    ): MemberProfile
}
