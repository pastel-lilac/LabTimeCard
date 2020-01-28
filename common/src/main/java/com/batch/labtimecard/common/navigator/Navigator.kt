package com.batch.labtimecard.common.navigator

import android.app.Activity

interface Navigator {
    fun Activity.navigateToMember()
    fun Activity.navigateToLog(member: String?)
    fun Activity.navigateToRegist()
}