package com.batch.labtimecard.ui

import com.airbnb.epoxy.TypedEpoxyController
import com.batch.labtimecard.model.Member
import com.batch.labtimecard.view.memberItemView

class MemberListController(private val callback: ClickListener) : TypedEpoxyController<List<Member>>() {

    interface ClickListener {
        fun itemClickListener(item: Member)
    }

    override fun buildModels(data: List<Member>) {
        data.forEachIndexed { index, member ->
            memberItemView {
                id(index)
                member(member)
                listener(callback)
            }
        }
    }
}