package com.example.hicabbie.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.click(function: (View) -> Unit) {
    this.setOnClickListener { function.invoke(this) }
}

fun Context.longToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}