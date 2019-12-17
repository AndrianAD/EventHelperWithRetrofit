package com.example.event_retrofit

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.SystemClock


object UtilsKotlin {

    var mLastClickTime: Long = 0

    @JvmStatic
    fun preventMultiClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }
}

