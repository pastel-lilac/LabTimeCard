package com.batch.labtimecard.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.batch.labtimecard.R
import com.batch.labtimecard.databinding.ItemMemberBinding
import com.batch.labtimecard.model.MemberData
import com.batch.labtimecard.ui.log.LogActivity
import com.batch.labtimecard.ui.memberlist.MemberListController
import com.batch.labtimecard.ui.memberlist.MemberListViewModel
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
            val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
            val dialogMessage: String
            val isActive = memberData.member?.active ?: false
            val activeColorInt: Int
            if (!isActive) {
                dialogMessage = "${memberData.member?.name}でログインします"
                activeColorInt = ContextCompat.getColor(context, R.color.online)
            } else {
                dialogMessage = "${memberData.member?.name}でログアウトします"
                activeColorInt = ContextCompat.getColor(context, R.color.offline)
            }
            builder.setTitle("確認")
            builder.setMessage(dialogMessage)
                .setPositiveButton("OK") { _, _ ->
                    listener?.itemClickListener(memberData)
                    binding.itemMemberLayout.setBackgroundColor(activeColorInt)
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
            builder.show()
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