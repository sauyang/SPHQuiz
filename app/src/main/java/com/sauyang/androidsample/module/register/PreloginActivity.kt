package com.sauyang.androidsample.module.register;

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sauyang.androidsample.R
import com.sauyang.androidsample.model.UserBean
import com.sauyang.androidsample.ui.SampleBaseActivity
import com.sauyang.androidsample.utils.UtilSharedPreferences
import com.sauyang.webservices.helpers.DialogManager
import kotlinx.android.synthetic.main.activity_prelogin.*


class PreloginActivity : SampleBaseActivity(), View.OnClickListener {

    private var userList: ArrayList<UserBean> = ArrayList<UserBean>()
    private val cacheTag: String = "user bean cache"

    override fun populateData(savedInstanceState: Bundle?) {
        super.populateData(savedInstanceState)
        //Retrieve Data From Storage
        showRegisteredList()
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                if(UtilSharedPreferences.isFastClick()){
                    //Do nothing
                }else {
                    if (TextUtils.isEmpty(etUsername.text.toString())) {
                        DialogManager.showAlertDialog(
                            this,
                            "Username is blank",
                            "Username cannot be blank.",
                            "ok"
                        )
                    } else if (TextUtils.isEmpty(etPassword.text.toString())) {
                        DialogManager.showAlertDialog(
                            this,
                            "Password is blank",
                            "Password cannot be blank.",
                            "Ok"
                        )

                    } else {
                        proceedToLoginScreen();
                    }
                }

            }
            R.id.tvFirstTime -> {
                if(UtilSharedPreferences.isFastClick()){
                    //Do Nothing
                }else {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivityForResult(intent, RegisterActivity.request_tag)
                }
            }

        }
    }

    private fun proceedToLoginScreen() {

        //TODO check password and username from saved object

        var isMatched = false

        if (!userList.isEmpty()) {

            userList.forEach { userBean ->
                if (userBean.username.equals(etUsername.text.toString(), ignoreCase = false) &&
                    userBean.password.equals(etPassword.text.toString(), ignoreCase = false))
                {
                    isMatched = true
                    val intent = Intent(this, MainMenuActivity::class.java)
                    intent.putExtra("isFromLogin", true)
                    startActivity(intent)
                }
            }
        }

        if(!isMatched)
            DialogManager.showAlertDialog(this,"Login Invalid", "Wrong Username or Password", "Ok")

    }

    override fun contentViewID(): Int {
       return R.layout.activity_prelogin
    }

    override fun populateLayout(v: View?) {
        ivLoadingIcon.setBackgroundResource(R.drawable.loading_animation)
        val drawableAnimaton = ivLoadingIcon.background as AnimationDrawable
        drawableAnimaton.start()

        tvFirstTime.setOnClickListener(this)
        btnLogin.setOnClickListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RegisterActivity.request_tag) {
                val userBean = data?.getSerializableExtra(RegisterActivity.request_data) as UserBean
                userList.add(userBean)
                storeDataFromLocal()
            }
        }
    }

    private fun storeDataFromLocal() {
        UtilSharedPreferences.getInstance(this)?.saveData(cacheTag, Gson().toJson(userList))
        UtilSharedPreferences.getInstance(this)!!.setIsRegisterDone(true)
    }

    //Retrieve userList from Storage
    private fun retrieveDataFromLocal() {
        val data = UtilSharedPreferences.getInstance(this)?.getData(cacheTag)
        val itemType = object : TypeToken<ArrayList<UserBean>>() {}.type
        this.userList = Gson().fromJson(data, itemType)
    }

    private fun showRegisteredList() {

        if (UtilSharedPreferences.getInstance(this)!!.isRegisterDone()) {
            //retrieve list from local storage
            retrieveDataFromLocal()
            return
        }
    }
}