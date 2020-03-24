package com.sauyang.androidsample.ui

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.sauyang.androidsample.R
import com.sauyang.webservices.helpers.DBSPreferenceUserDefaults
import com.sauyang.webservices.helpers.DialogManager
import com.sauyang.webservices.utils.SUtils
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    val isPermissionCheckRequire = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        permissionCheck(isPermissionCheckRequire)
    }
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    //Vkey permissions
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun arePermissionsEnabled(): Boolean {
        for (permission in permissions) {
            if (checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestMultiplePermission() {
        val remainingPermissions: MutableList<String> =
            ArrayList()
        for (permission in permissions) {
            if (checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission)
            }
        }
        requestPermissions(
            remainingPermissions.toTypedArray(),
            ANDROID_PERMISSION_REQ_CODE
        )
    }

    companion object{

        const val OVERLAY_PERMISSION_REQ_CODE = 9999
        const val ANDROID_PERMISSION_REQ_CODE = 8888
        var isTablet = false
        var permissionPopUpHasShown = false
        const val moreURL = "http://www.howtogeek.com/271519/how-to-fix-the-screen-overlay-detected-error-on-android/"
    }

    protected fun permissionCheck(isPermissionCheck: Boolean) {
        if (!isPermissionCheck) {
            return
        }
        //check Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!arePermissionsEnabled()) { //show prompt or close app
                requestMultiplePermission()
            } else {
                if (hasOverlayPermission(this)) {
                    actionPermissionEnable()
                }
            }
        } else {
            actionPermissionEnable()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data
        )
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE && resultCode == Activity.RESULT_CANCELED) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
                DialogManager.showProgressDialog(this)
                Handler().postDelayed({
                    DialogManager.hideProgressDialog()
                    if (hasOverlayPermission(this@BaseActivity)) {
                        actionPermissionEnable()
                    }
                }, 2000)
            } else {
                if (hasOverlayPermission(this@BaseActivity)) {
                    actionPermissionEnable()
                }
            }
        }
    }

    fun actionPermissionEnable(){
        // override action
    }

    // Check whether the permission is available or not
    @TargetApi(23)
    fun hasOverlayPermission(@NonNull context: Context?): Boolean {
        val hasPermission =
            Build.VERSION.SDK_INT < 23 || SUtils.canDrawOverlays(context)
        if (!hasPermission) {
            val ask: Boolean = java.lang.Boolean.valueOf(
                DBSPreferenceUserDefaults.getPreference(
                    context,
                    "DRAW_OVER_ASK",
                    true.toString()
                )
            )
            if (ask && !permissionPopUpHasShown) {
                permissionPopUpHasShown = true
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Permission")
                builder.setMessage("Please allow permissions to continue")
                builder.setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialog, which ->
                        val packageName = packageName
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName")
                        )
                        startActivityForResult(
                            intent,
                            OVERLAY_PERMISSION_REQ_CODE
                        ) //any request code
                        permissionPopUpHasShown = false
                    })
                builder.setNeutralButton("More",
                    DialogInterface.OnClickListener { dialog, which ->
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(moreURL)
                        startActivity(i)
                        permissionPopUpHasShown = false
                    })
                builder.setCancelable(false)
                DialogManager.showAlertDialogBuilder(builder)
            }
        } else return true
        return false
    }

}