package com.batch.labtimecard.ui.memberlist

import com.airbnb.epoxy.TypedEpoxyController
import com.batch.labtimecard.model.MemberData
import com.batch.labtimecard.view.memberItemView

class MemberListController(private val callback: ClickListener) : TypedEpoxyController<List<MemberData>>() {

    interface ClickListener {
        fun itemClickListener(item: MemberData)
        fun buttonClickListener(item: MemberData)
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