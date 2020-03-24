package com.sauyang.androidsample.module.register;

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.get
import com.sauyang.androidsample.R
import com.sauyang.androidsample.model.UserBean

import com.sauyang.androidsample.ui.SampleBaseActivity
import com.sauyang.androidsample.ui.widget.STYLEditText
import com.sauyang.androidsample.utils.UtilStorage
import com.sauyang.webservices.helpers.DialogManager
import kotlinx.android.synthetic.main.activity_edit.*


class EditActivity  : SampleBaseActivity() {

    private var userBean : UserBean = UserBean()
    private var isEditable : Boolean = false

    companion object {
        val request_tag = 0x1001
        val request_data = "user data detail bean"
    }

    override fun populateData(savedInstanceState: Bundle?) {
        super.populateData(savedInstanceState)
        userBean = intent.getSerializableExtra(request_data) as UserBean
    }

    private fun enableEditable(){

        etUsername.enableKeyListener()
        etPassword.enableKeyListener()
        etFirstName.enableKeyListener()
        etLastName.enableKeyListener()
        etEmailAddress.enableKeyListener()
        etAge.enableKeyListener()
        etAge.getEtDescription().inputType = InputType.TYPE_CLASS_NUMBER
        etHobbies.enableKeyListener()


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


        ivUpload.setOnClickListener({
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RegisterActivity.pick_img_data)})


    }

    private fun disableEditable(){

        etUsername.disableKeyListener()
        etPassword.disableKeyListener()
        etFirstName.disableKeyListener()
        etLastName.disableKeyListener()
        etEmailAddress.disableKeyListener()
        etAge.disableKeyListener()
        etHobbies.disableKeyListener()
        ivUpload.setOnClickListener(null)
    }

    private fun backToMainActivity(){
        val returnIntent = Intent()
        returnIntent.putExtra(request_data,userBean)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun contentViewID(): Int {
       return R.layout.activity_edit
    }

    override fun populateLayout(v: View?) {
        etAge.getEtDescription().inputType = InputType.TYPE_CLASS_NUMBER

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        etUsername.getEtDescription().setText(userBean.username)
        etPassword.getEtDescription().setText(userBean.password)

        etFirstName.getEtDescription().setText(userBean.firstname)
        etLastName.getEtDescription().setText(userBean.lastname)
        etEmailAddress.getEtDescription().setText(userBean.emailaddress)
        etAge.getEtDescription().setText(userBean.age)
        etHobbies.getEtDescription().setText(userBean.hobbies)


        if(!TextUtils.isEmpty(userBean.imagePath)){
            ivUpload.setImageURI(Uri.parse(userBean.imagePath))
        }

        if(isEditable){
            enableEditable()
        }else{
            disableEditable()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == RegisterActivity.pick_img_data){
                val selectedImage = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

                if(bitmap != null){
                    UtilStorage(baseContext,object : UtilStorage.StorageListener{
                        override fun onSuccess(path: String) {
                            // set new path
                            userBean.imagePath = path
                            ivUpload.setImageBitmap(bitmap)
                        }
                        override fun onFail(message: String) {
                            // do nothing
                        }
                    }).saveToInternalStorage(bitmap)

                }
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_detail, menu)

        val ic_save = resources.getDrawable(R.drawable.ic_save)
        val ic_edit = resources.getDrawable(R.drawable.ic_edit)
        if(isEditable){
            menu.get(0).icon = ic_save
        }else{
            menu.get(0).icon = ic_edit
        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.i_edit -> {
                if(isEditable){


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
                        DialogManager.showAlertDialog(this, "Congratulation", "Edit is done",
                            "OK",
                            object : DialogInterface.OnClickListener {

                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    backToMainActivity()
                                }
                            })
                    }

                    return super.onOptionsItemSelected(item)
                }
                isEditable = !isEditable
                invalidateOptionsMenu();
                populateLayout(null)

            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}