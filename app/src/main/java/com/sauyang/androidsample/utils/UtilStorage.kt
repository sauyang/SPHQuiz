package com.sauyang.androidsample.utils;

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UtilStorage(context: Context, storageListener : StorageListener){

    private val context = context
    private val storageListener = storageListener

    fun saveToInternalStorage(bitmapImage: Bitmap) {
        val cw = ContextWrapper(context.getApplicationContext())

        // path to /data/data/yourapp/app_data/imageDir
        val dirc = cw.getDir("imageDir", Context.MODE_PRIVATE)

        val time = System.currentTimeMillis()

        // Create imageDir
        val path = File(dirc, "Image$time.jpg")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(path)

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)

        } catch (e: Exception) {
            e.printStackTrace()
            storageListener.onFail(e.toString())
        } finally {
            try {
                fos!!.close()
                storageListener.onSuccess(path.absolutePath)
            } catch (e: IOException) {
                e.printStackTrace()
                storageListener.onFail(e.toString())
            }
        }
        //return MyDirectory.getAbsolutePath();
    }

    fun getInternalStorageData() {

    }

    interface StorageListener{
        fun onSuccess(path : String)
        fun onFail(message : String)
    }

}