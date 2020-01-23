package com.batch.labtimecard.ui

import com.airbnb.epoxy.TypedEpoxyController
import com.batch.labtimecard.model.Member
import com.batch.labtimecard.model.MemberData
import com.batch.labtimecard.view.memberItemView

class MemberListController(private val callback: ClickListener) : TypedEpoxyController<List<MemberData>>() {

    interface ClickListener {
        fun itemClickListener(item: MemberData)
    }

    override fun buildModels(data: List<MemberData>) {
        data.forEachIndexed { index, memberData ->
            memberItemView {
                id(index)
                member(memberData)
                listener(callback)
            }
        }
    }
}