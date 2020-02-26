package com.batch.labtimecard.data.api

import com.batch.labtimecard.data.model.GroupMembers
import com.batch.labtimecard.data.model.MemberProfile
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST(POST_MESSAGE)
    suspend fun postSlackMessage(
        @Query("token") token: String,
        @Query("channel") channel: String,
        @Query("text") text: String
    )
}
