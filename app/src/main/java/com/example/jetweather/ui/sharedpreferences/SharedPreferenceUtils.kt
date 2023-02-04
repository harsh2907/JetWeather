package com.example.jetweather.ui.sharedpreferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtils(
    private val activity: Activity
) {
    private  val isFirstTime = "IS_FIRST_TIME"
    private val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

    fun setFirstTimeDone() {
        with(sharedPref.edit()) {
            putBoolean(isFirstTime, false)
            apply()
        }
    }

    fun isFirstTime():Boolean {
        return sharedPref.getBoolean(isFirstTime,true)
    }

}