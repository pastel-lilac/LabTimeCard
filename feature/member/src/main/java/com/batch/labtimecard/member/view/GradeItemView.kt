package com.batch.labtimecard.member.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.batch.labtimecard.member.R
import com.batch.labtimecard.member.databinding.ItemGradeBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GradeItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ItemGradeBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.item_grade, this, true)
    }

    @TextProp
    fun setGrade(grade: CharSequence) {
        binding.textGrade.text = grade
    }
}