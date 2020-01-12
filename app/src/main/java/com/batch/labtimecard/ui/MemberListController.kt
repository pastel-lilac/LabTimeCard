package com.batch.labtimecard.ui

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.batch.labtimecard.ItemMemberBindingModel_
import com.batch.labtimecard.itemMember
import com.batch.labtimecard.model.Member

class MemberListController(private val callback: ClickListener) : TypedEpoxyController<List<Member>>() {

    interface ClickListener {
        fun itemClickListenter(item: Member)
    }

    override fun buildModels(data: List<Member>) {
        data.forEach { member ->
            itemMember {
                id(member.hashCode())
                member(member)
                itemClickListener(View.OnClickListener { callback.itemClickListenter(member)})
            }
        }
    }
}