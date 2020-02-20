package com.batch.labtimecard.data.model

import java.io.Serializable

data class Member(
    val name: String? = "",
    val grade: String? = "",
    val slackId: String? = "",
    val iconUrl: String? = null,
    val active: Boolean? = null
) : Serializable