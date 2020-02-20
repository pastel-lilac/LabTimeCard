package com.batch.labtimecard.common

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.api.load
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setImage")
fun ImageView.setImage(url: String?) {
    load(url)
}

@BindingAdapter("yearAndMonth")
fun TextView.setDateForHe(date: Date?) {
    date?.let {
        text = SimpleDateFormat("yyyy年M月", Locale.JAPAN).format(it)
    }
}

@BindingAdapter("yearAndMonthAndDay")
fun TextView.setDateForMonth(date: Date?) {
    date?.let {
        text = SimpleDateFormat("yyyy年M月d日", Locale.JAPAN).format(it)
    }
}

val Date.dateString: String
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN).format(this)


val Date.timeString: String
    get() = SimpleDateFormat("HH:mm:ss", Locale.JAPAN).format(this)


val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))