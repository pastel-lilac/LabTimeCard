package com.batch.labtimecard.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.batch.labtimecard.R
import com.batch.labtimecard.databinding.ItemMemberBinding
import com.batch.labtimecard.model.MemberData
import com.batch.labtimecard.ui.log.LogActivity
import com.batch.labtimecard.ui.memberlist.MemberListController
import kotlinx.android.synthetic.main.item_member.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MemberItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ItemMemberBinding
    private lateinit var memberData: MemberData

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.item_member, this, true)
    }

    @ModelProp
    fun setMember(memberData: MemberData) {
        this.memberData = memberData
        binding.memberData = memberData
    }

    @CallbackProp
    fun setListener(listener: MemberListController.ClickListener?) {
        binding.card.setOnClickListener {
            listener?.itemClickListener(memberData)
            val isActive = memberData.member?.active ?: false
            if (!isActive) {
                binding.itemMemberLayout.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.online)
                )
            } else {
                binding.itemMemberLayout.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.offline)
                )
            }
        }
    }

    @CallbackProp
    fun buttonSetListener(listener: MemberListController.ClickListener?) {
        button_more.setOnClickListener {
            listener?.buttonClickListener(memberData)
            val popup = PopupMenu(context, button_more)
            popup.menuInflater.inflate(R.menu.main, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { itemMenu ->
                when (itemMenu.itemId) {
                    R.id.show_log -> {
                        val intent = Intent(context, LogActivity::class.java)
                        intent.putExtra("memberKey", memberData.key)
                        context.startActivity(intent)
                    }
                }
                true
            })
            popup.show()
        }
    }
}