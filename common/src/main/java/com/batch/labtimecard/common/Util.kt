package com.batch.labtimecard.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load

@BindingAdapter("setImage")
fun ImageView.setImage(url: String?) {
    load(url)
}
