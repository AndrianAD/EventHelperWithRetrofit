package com.example.event_retrofit

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.SystemClock
import androidx.core.app.ActivityCompat


object UtilsKotlin {

    @kotlin.jvm.JvmField
    var mLastClickTime: Long = 0
    @kotlin.jvm.JvmField
    var STORAGE_PERMISSION_CODE:Int = 99

    @JvmStatic
    fun preventMultiClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    @JvmStatic
    fun requestPermissions(activity: Activity, context: Context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.RECORD_AUDIO
                )
        ) {
            AlertDialog.Builder(context)
                    .setTitle("")
                    .setMessage("")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.RECORD_AUDIO), STORAGE_PERMISSION_CODE
                        )
                    }
                    .setNegativeButton(
                           "NO"
                    ) { dialog, _ -> dialog.dismiss() }
                    .create().show()

        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO), STORAGE_PERMISSION_CODE
            )
        }
    }
}

