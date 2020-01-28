package com.batch.labtimecard.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setImage")
fun ImageView.setImage(labName: String?) {
    if (labName == context.getString(R.string.common_nkmr_lab)) {
        setImageResource(R.drawable.common_nakam_logo)
    } else {
        setImageResource(R.drawable.common_inam_logo)
    }
}
