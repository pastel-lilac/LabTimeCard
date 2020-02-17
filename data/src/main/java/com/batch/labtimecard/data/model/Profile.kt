package com.batch.labtimecard.data.model

data class Profile(
    val avatar_hash: String?,
    val display_name: String?,
    val display_name_normalized: String?,
    val email: String?,
    val first_name: String?,
    val image_1024: String?,
    val image_192: String?,
    val image_24: String?,
    val image_32: String?,
    val image_48: String?,
    val image_512: String?,
    val image_72: String?,
    val image_original: String?,
    val is_custom_image: Boolean?,
    val last_name: String?,
    val phone: String?,
    val real_name: String?,
    val real_name_normalized: String?,
    val skype: String?,
    val status_emoji: String?,
    val status_expiration: Int?,
    val status_text: String?,
    val status_text_canonical: String?,
    val title: String?
)