package com.madcamp.eattogether

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId")
    var userId: String,
    /*
    @SerializedName("name")
    var name: String,
     */
    @SerializedName("userPhoneNum")
    var userPhoneNum: String
)
