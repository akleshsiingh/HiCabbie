package com.example.hicabbie.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionManager {

    companion object {
        fun isPermissionGranted(context: Context, permission: String): Boolean {
            return (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED)
        }

        fun requestPermission(context: Context, permission: String, code: Int) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission),code)
        }
    }

}