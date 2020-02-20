package com.batch.labtimecard.data.model

import java.io.Serializable

data class MemberData(
    val key: String?,
    val member: Member?
) : Serializable