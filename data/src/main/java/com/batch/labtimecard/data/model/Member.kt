package com.batch.labtimecard.data.model

data class Member(
    val name: String? = "",
    val grade: String? = "",
    val slackId: String? = "",
    val iconUrl: String? = null,
    val active: Boolean? = null
)