package com.batch.labtimecard

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setImage")
fun ImageView.setImage(labName: String) {
    if (labName == context.getString(R.string.nkmr_lab)) {
        setImageResource(R.drawable.nakam_logo)
    } else {
        setImageResource(R.drawable.inam_logo)
    }
}
