package com.sauyang.androidsample.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserBean : Serializable {

    @SerializedName("@id")
    var id: String = ""
        get() = field
        set(value) {
            field = value
        }
    var imagePath: String = ""
        get() = field
        set(value) {
            field = value
        }
    var username: String = ""
        get() = field
        set(value) {
            field = value
        }

    var password:String =""
        get() = field
        set(value) {
            field = value
        }

    var firstname: String = ""
        get() = field
        set(value) {
            field = value
        }
    var lastname: String = ""
        get() = field
        set(value) {
            field = value
        }
    var emailaddress: String = ""
        get() = field
        set(value) {
            field = value
        }
    var age: String = ""
        get() = field
        set(value) {
            field = value
        }
    var hobbies: String = ""
        get() = field
        set(value) {
            field = value
        }

    constructor()

    constructor(
        id: String,
        imagePath: String,
        username: String,
        password: String,
        firstname: String,
        lastname: String,
        emailaddress: String,
        age: String,
        hobbies: String
    ) {
        this.id = id
        this.imagePath = imagePath
        this.username = username
        this.password = password
        this.firstname = firstname
        this.lastname = lastname
        this.emailaddress = emailaddress
        this.age = age
        this.hobbies = hobbies
    }


}