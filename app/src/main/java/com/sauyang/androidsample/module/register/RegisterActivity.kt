package com.sauyang.androidsample.module.register;

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.MediaStore
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sauyang.androidsample.R
import com.sauyang.androidsample.model.UserBean

import com.sauyang.androidsample.ui.SampleBaseActivity
import com.sauyang.androidsample.ui.widget.STYLEditText
import com.sauyang.androidsample.utils.UtilStorage
import com.sauyang.webservices.helpers.DialogManager
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : SampleBaseActivity() {

    private var userBean: UserBean = UserBean()


    companion object {
        val request_tag = 0x1000
        val request_data = "user data bean"
        val pick_img_data = 0x1010
    }



    override fun contentViewID(): Int {
       return R.layout.activity_register
    }

    override fun populateLayout(v: View?) {
        etAge.getEtDescription().inputType = InputType.TYPE_CLASS_NUMBER

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        etUsername.getEtDescription().addTextChangedListener(object : STYLEditText.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userBean.username = p0.toString()
            }

        })


        etPassword.getEtDescription().addTextChangedListener(object : STYLEditText.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userBean.password = p0.toString()
            }

        })
        etFirstName.getEtDescription().addTextChangedListener(object : STYLEditText.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userBean.firstname = p0.toString()
            }

        })
        etLastName.getEtDescription().addTextChangedListener(object : STYLEditText.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userBean.lastname = p0.toString()
            }
        })
        etEmailAddress.getEtDescription().addTextChangedListener(object : STYLEditText.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userBean.emailaddress = p0.toString()
            }
        })

        etAge.getEtDescription().addTextChangedListener(object : STYLEditText.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userBean.age = p0.toString()
            }
        })

        etHobbies.getEtDescription().addTextChangedListener(object : STYLEditText.TextChanged {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userBean.hobbies = p0.toString()
            }
        })

        imgUpload.setOnClickListener({
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), pick_img_data)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == pick_img_data) {
                val selectedImage = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

                if (bitmap != null) {
                    UtilStorage(baseContext, object : UtilStorage.StorageListener {
                        override fun onSuccess(path: String) {
                            // set new path
                            userBean.imagePath = path
                            imgUpload.setImageBitmap(bitmap)
                        }

                        override fun onFail(message: String) {
                            // do nothing
                        }
                    }).saveToInternalStorage(bitmap)

                }

            }
        }
    }

    private fun backToMainActivity() {
        userBean.id = System.currentTimeMillis().toString()
        val returnIntent = Intent()
        returnIntent.putExtra(request_data, userBean)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_add, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.i_save -> {

                if (etUsername.getEtDescription().text.toString().isEmpty()) {
                    DialogManager.showAlertDialog(
                        this,
                        "Username is blank",
                        "Username cannot be blank.",
                        "ok"
                    )
                } else if (etPassword.getEtDescription().text.toString().isEmpty()) {
                    DialogManager.showAlertDialog(
                        this,
                        "Password is blank",
                        "Password cannot be blank.",
                        "ok"
                    )
                } else if(etFirstName.getEtDescription().text.toString().isEmpty()){
                    DialogManager.showAlertDialog(
                        this,
                        "FirstName is blank",
                        "FirstName cannot be blank.",
                        "ok"
                    )
                }else if(etLastName.getEtDescription().text.toString().isEmpty()){
                    DialogManager.showAlertDialog(
                        this,
                        "LastName is blank",
                        "LastName cannot be blank.",
                        "ok"
                    )
                }else if(etEmailAddress.getEtDescription().text.toString().isEmpty()){
                    DialogManager.showAlertDialog(
                        this,
                        "Email is blank",
                        "Email cannot be blank.",
                        "ok"
                    )
                }else if(etAge.getEtDescription().text.toString().isEmpty()){
                    DialogManager.showAlertDialog(
                        this,
                        "Age is blank",
                        "Age cannot be blank.",
                        "ok"
                    )
                }else if(etHobbies.getEtDescription().text.toString().isEmpty()){
                    DialogManager.showAlertDialog(
                        this,
                        "Hobbies is blank",
                        "Hobbies cannot be blank.",
                        "ok"
                    )
                }else {
                    DialogManager.showAlertDialog(this, "Congratulation", "Registration is done",
                        "OK",
                        object : DialogInterface.OnClickListener {

                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                backToMainActivity()
                            }
                        })
                }

            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}