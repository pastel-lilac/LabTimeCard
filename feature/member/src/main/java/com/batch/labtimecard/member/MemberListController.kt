package com.batch.labtimecard.member

import com.airbnb.epoxy.TypedEpoxyController
import com.batch.labtimecard.data.model.MemberData
import com.batch.labtimecard.member.view.gradeItemView
import com.batch.labtimecard.member.view.memberItemView


class MemberListController(private val callback: ClickListener) :
    TypedEpoxyController<List<MemberData>>() {

    interface ClickListener {
        fun itemClickListener(item: MemberData)
        fun buttonClickListener(item: MemberData)
    }

    override fun buildModels(data: List<MemberData>) {
        var previousGrade = ""
        data.forEachIndexed { index, memberData ->
            val currentGrade = memberData.member?.grade ?: ""
            if (currentGrade.isNotEmpty() && currentGrade != previousGrade) {
                gradeItemView {
                    id(memberData.member?.grade)
                    grade(currentGrade)
                    spanSizeOverride { _, _, _ -> SPAN_COUNT }
                }
            }
            memberItemView {
                id(index)
                member(memberData)
                listener(callback)
                spanSizeOverride { _, _, _ -> 1 }
            }
            previousGrade = currentGrade
        }
    }

    companion object {
        const val SPAN_COUNT = 5
    }
}