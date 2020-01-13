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
        setImageResource(R.drawable.sarimaru)
    } else {
        setImageResource(R.drawable.enako)
    }
}

@BindingAdapter("visible")
fun View.visible(memberName: String) {
    Log.d("ORENOTAG", "hehee")
    val myPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    myPref.registerOnSharedPreferenceChangeListener { pref: SharedPreferences, key: String ->
        Log.d("ORENOTAG", "registerの中")
        if (key == "${memberName}isExisting}") {
            Log.d("ORENOTAG", "true!!：key=${key}, myPrefKey=${memberName}isExisting")
            val isExisting = pref.getBoolean(key, false)
            when (isExisting) {
                true -> visibility = View.GONE
                false -> visibility = View.VISIBLE
            }
        } else {
            Log.d("ORENOTAG", "false!!：key=${key}, myPrefKey=${memberName}isExisting")
        }
    }
}
