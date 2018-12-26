package com.example.hicabbie.utils

import android.util.Log

abstract class Logger {

    companion object {
       private const val active = true
        fun e(tag: String, value: String) {
            if (active)
                Log.e(tag, value)
        }
    }
}