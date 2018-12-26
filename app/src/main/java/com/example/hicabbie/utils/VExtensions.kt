package com.example.hicabbie.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun ViewGroup.inflateView(layout: Int) = LayoutInflater.from(this.context).inflate(layout, this, false)


var View.vis: Boolean
    get() {
        return this.visibility == View.VISIBLE
    }
    set(value) {
        if (value) {
            if (this.visibility != View.VISIBLE)
                this.visibility = View.VISIBLE
        } else {
            if (this.visibility == View.VISIBLE)
                this.visibility = View.INVISIBLE
        }
    }