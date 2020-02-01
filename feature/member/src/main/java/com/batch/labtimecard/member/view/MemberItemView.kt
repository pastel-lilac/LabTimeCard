package com.batch.labtimecard.member.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.batch.labtimecard.data.model.MemberData
import com.batch.labtimecard.log.LogActivity
import com.batch.labtimecard.member.R
import com.batch.labtimecard.member.databinding.ItemMemberBinding
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
        val isActive = memberData.member?.active ?: return
        val activeColorInt = if (isActive) {
            ContextCompat.getColor(context, R.color.online)
        } else {
            ContextCompat.getColor(context, R.color.offline)
        }
        binding.itemMemberLayout.setBackgroundColor(activeColorInt)
    }

    @CallbackProp
    fun setListener(listener: com.batch.labtimecard.member.MemberListController.ClickListener?) {
        binding.card.setOnClickListener {
            val dialogMessage: String
            val isActive = memberData.member?.active ?: false
            val activeColorInt = if (!isActive) {
                dialogMessage = "${memberData.member?.name}でログインします"
                ContextCompat.getColor(context, R.color.online)
            } else {
                dialogMessage = "${memberData.member?.name}でログアウトします"
                ContextCompat.getColor(context, R.color.offline)
            }
            AlertDialog.Builder(context, R.style.common_MyAlertDialogStyle).apply {
                setTitle("確認")
                setMessage(dialogMessage)
                setPositiveButton("OK") { _, _ ->
                    listener?.itemClickListener(memberData)
                    binding.itemMemberLayout.setBackgroundColor(activeColorInt)
                }
                setNegativeButton("Cancel") { _, _ ->
                }
            }.show()
        }
    }

    @CallbackProp
    fun buttonSetListener(listener: com.batch.labtimecard.member.MemberListController.ClickListener?) {
        button_more.setOnClickListener {
            listener?.buttonClickListener(memberData)
            val popup = PopupMenu(context, button_more)
            popup.menuInflater.inflate(R.menu.member, popup.menu)
            popup.setOnMenuItemClickListener { itemMenu ->
                when (itemMenu.itemId) {
                    R.id.show_log -> {
                        val intent = LogActivity.createIntent(
                            context as Activity,
                            memberData.key
                        )
                        context.startActivity(intent)
                    }
                }
                true
            }
            popup.show()
        }
    }
}