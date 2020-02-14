package com.batch.labtimecard.navigator

import android.app.Activity
import com.batch.labtimecard.common.navigator.Navigator
import com.batch.labtimecard.log.LogActivity
import com.batch.labtimecard.member.MemberListActivity
import com.github.pastel_lilac.register.RegisterUserActivity

class NavigatorImpl : Navigator {
    override fun Activity.navigateToLog(member: String?) {
        val intent = LogActivity.createIntent(this, member)
        startActivity(intent)
    }

    override fun Activity.navigateToMember() {
        val intent = MemberListActivity.createIntent(this)
        startActivity(intent)
    }

    override fun Activity.navigateToRegister() {
        val intent = RegisterUserActivity.createIntent(this)
        startActivity(intent)
    }
}