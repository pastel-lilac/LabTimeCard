package com.batch.labtimecard.navigator

import android.app.Activity
import com.batch.labtimecard.common.navigator.Navigator
import com.batch.labtimecard.data.model.MemberData
import com.batch.labtimecard.log.LogActivity
import com.batch.labtimecard.member.MemberListActivity

class NavigatorImpl : Navigator {

    override fun Activity.navigateToMember() {
        val intent = MemberListActivity.createIntent(this)
        startActivity(intent)
    }
}