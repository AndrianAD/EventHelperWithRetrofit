package com.example.event_retrofit

import android.app.Application
import android.content.Context
import android.content.SharedPreferences


class App : Application() {

    companion object {
        lateinit var instance: App
        private var sharedPreferences: SharedPreferences? = null

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getMySharedPreferences(): SharedPreferences? {
        return if (sharedPreferences == null) {
            getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        } else
            sharedPreferences
    }

    fun clearPreferances() {
        getMySharedPreferences()?.edit()?.remove(SHARED_EMAIL)?.remove(SHARED_PASSWORD)?.commit()
//        etPassword.setText("");
//        etEmail.setText("");
    }


}

