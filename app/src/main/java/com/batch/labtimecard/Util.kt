package com.batch.labtimecard

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setImage")
fun ImageView.setImage(labName: String) {
    if (labName == context.getString(R.string.nkmr_lab)) {
        setImageResource(R.drawable.sarimaru)
    } else {
        setImageResource(R.drawable.enako)
    }
}