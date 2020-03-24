package com.sauyang.androidsample.ui

import android.content.Context
import com.sauyang.webservices.helpers.AndroidApplication

class AppApplication : AndroidApplication() {

    companion object {
        var instance: Context? = null

        @JvmStatic fun getInstance() : AppApplication?{
            return instance as AppApplication?
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
    }

}