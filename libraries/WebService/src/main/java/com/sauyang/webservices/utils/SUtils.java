package com.sauyang.webservices.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sauyang.webservices.helpers.AndroidApplication;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import webservices.debug.LogDbug;

public class SUtils {

    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && android.provider.Settings.canDrawOverlays(context)) return true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//USING APP OPS MANAGER
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (manager != null) {
                try {
                    int result = manager.checkOp(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, Binder.getCallingUid(), context.getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED;
                } catch (Exception ignore) {
                }
            }
        }
        try {//IF This Fails, we definitely can't do it
            WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (mgr == null) return false; //getSystemService might return null
            View viewToAdd = new View(context);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0, android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
            viewToAdd.setLayoutParams(params);
            mgr.addView(viewToAdd, params);
            mgr.removeView(viewToAdd);
            return true;
        } catch (Exception ignore) {
        }
        return false;

    }

    public static JSONObject getMobileDataJSON() throws JSONException {

        String mobileDataJson = null;
        try {
            FileInputStream fileInputStream = AndroidApplication.getAppContext().openFileInput("mobiledata.json");

            mobileDataJson = IOUtils.toString(fileInputStream);
        } catch (IOException e) {
            Log.i("SeeString",""+ mobileDataJson);
            LogDbug.printStackTrace(e);
        }
        if (mobileDataJson == null){
            mobileDataJson = readStringFromAssetsFile("mobiledata.json");
        }

        if (mobileDataJson != null)
            return new JSONObject(mobileDataJson);
        return null;
    }

    private static String readStringFromAssetsFile(String fileName){
        AssetManager assetManager = AndroidApplication.getAppContext().getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            return IOUtils.toString(inputStream);
        }catch (IOException exp){
            LogDbug.printStackTrace(exp);
        }
        return null;
    }

}
