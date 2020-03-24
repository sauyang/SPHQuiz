package com.sauyang.androidsample.utils;

import android.content.Context
import android.content.SharedPreferences

class UtilSharedPreferences  {


    private var sharedPreferences : SharedPreferences? = null

    constructor(sharedPreferences: SharedPreferences?) {
        this.sharedPreferences = sharedPreferences
    }

    companion object  {

        @Volatile private var instance: UtilSharedPreferences? = null
        private var sharedPreferences: SharedPreferences? = null
        private var tagKey = "UtilSharedPreferences"
        var lastClick: Long = 0
        val clickFastPrevent = 900

        fun getInstance(context: Context, tag : String = tagKey) : UtilSharedPreferences? {

            if (context != null) {
                sharedPreferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
                instance = UtilSharedPreferences(sharedPreferences)
            }
            return instance
        }


        fun isFastClick(): Boolean {
            val currentTime = System.currentTimeMillis()
            if (Math.abs(currentTime - lastClick) < clickFastPrevent)
                return true

            lastClick = currentTime
            return false
        }
    }

    fun setIsRegisterDone(boolean: Boolean) {
        saveData("isRegisterDone",boolean.toString())
    }

    fun isRegisterDone() : Boolean{
        val result = getData("isRegisterDone")
        if(result.equals("true", ignoreCase = true)){
            return true
        }
        return result!!.toBoolean()
    }



    fun saveData(key: String, value: String) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun getData(key: String): String? {
        return if (sharedPreferences != null) {
            sharedPreferences!!.getString(key, "false")
        } else ""
    }
}
