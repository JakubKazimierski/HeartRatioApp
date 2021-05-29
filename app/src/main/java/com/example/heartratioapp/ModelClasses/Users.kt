package com.example.heartratioapp.ModelClasses

class Users {


    private var uid: String = ""
    private var username: String = ""
    private var ratio: String = ""



    constructor()

    constructor(uid: String, username: String, ratio: String) {

        this.uid = uid
        this.username = username
        this.ratio = ratio
    }


    fun getUID(): String?{
        return uid
    }

    fun setUID(uid: String){
        this.uid = uid
    }

    fun getUserName(): String?{
        return username
    }

    fun setUserName(username: String){
        this.username = username
    }

    fun getRatio(): String?{
        return ratio
    }

    fun setRatio(ratio: String){
        this.ratio = ratio
    }


}