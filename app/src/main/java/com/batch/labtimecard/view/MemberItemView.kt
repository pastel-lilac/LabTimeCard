package com.batch.labtimecard.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.batch.labtimecard.R
import com.batch.labtimecard.databinding.ItemMemberBinding
import com.batch.labtimecard.model.Member
import com.batch.labtimecard.ui.MemberListController

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MemberItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ItemMemberBinding

    private lateinit var member: Member

    // FIXME ログイン状態はMemberListViewModelで管理する
    private var isClicked = false

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.item_member, this, true)
    }

    @ModelProp
    fun setMember(member: Member) {
        this.member = member
        binding.member = member
    }

    @CallbackProp
    fun setListener(listener: MemberListController.ClickListener?) {
        binding.card.setOnClickListener {
            listener?.itemClickListener(member)
            if (!isClicked) {
                binding.itemMemberLayout.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.colorPrimary)
                )
            }else {
                binding.itemMemberLayout.setBackgroundColor(
                    ContextCompat.getColor(context, android.R.color.darker_gray)
                )
            }
            isClicked = !isClicked
        }
    }
}