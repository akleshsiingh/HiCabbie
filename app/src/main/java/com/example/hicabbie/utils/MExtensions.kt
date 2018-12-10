package com.example.hicabbie.utils

import android.view.View

fun View.click(function: (View) -> Unit) {
    this.setOnClickListener { function.invoke(this) }
}