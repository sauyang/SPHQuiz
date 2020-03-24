package com.sauyang.webservices.helpers

import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

abstract class AndroidApplication : Application() {

    companion object {
        var appContext: WeakReference<Context>? = null
        var instance: Context? = null

        @JvmStatic fun getInstance() : AndroidApplication?{
            return instance as AndroidApplication?
        }

        @JvmStatic fun getAppContext(): Context? {
            return appContext?.get()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = WeakReference<Context>(getApplicationContext());
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
    }




}