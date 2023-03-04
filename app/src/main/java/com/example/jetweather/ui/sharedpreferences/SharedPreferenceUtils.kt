package com.example.jetweather.ui.sharedpreferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtils(
    context: Context
) {
    private  val isFirstTime = "IS_FIRST_TIME"
    private val tempMode = "Celsius"
    private val windMode = "ms"

    private val sharedPref: SharedPreferences = context.getSharedPreferences("DEFAULT",Context.MODE_PRIVATE)

    fun setFirstTimeDone() {
        with(sharedPref.edit()) {
            putBoolean(isFirstTime, false)
            apply()
        }
    }
    fun isFirstTime():Boolean {
        return sharedPref.getBoolean(isFirstTime,true)
    }

    fun getTemp()=  sharedPref.getInt(tempMode,0)
    fun setTemp(index:Int){
        with(sharedPref.edit()){
            putInt(tempMode,index)
            apply()
        }
    }

    fun getWind()=  sharedPref.getInt(windMode,0)
    fun setWind(index: Int){
        with(sharedPref.edit()){
            putInt(windMode,index)
            apply()
        }
    }


}