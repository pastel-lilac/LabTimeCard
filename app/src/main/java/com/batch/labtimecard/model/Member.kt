package com.batch.labtimecard.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Member(
    val name: String? = "",
    val affiliationLabName: String? = ""
)