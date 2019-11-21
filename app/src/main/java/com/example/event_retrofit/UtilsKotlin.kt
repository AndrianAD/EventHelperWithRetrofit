package com.example.event_retrofit

import android.os.SystemClock


class UtilsKotlin {

    var mLastClickTime: Long = 0

    fun preventMultiClick(): Boolean {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }
}

