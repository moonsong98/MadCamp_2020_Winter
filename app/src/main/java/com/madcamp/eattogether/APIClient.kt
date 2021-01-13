package com.madcamp.eattogether

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class APIClient {
    companion object{
        private lateinit var retrofit: Retrofit
        fun getClient():Retrofit {
            /*
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client =
                OkHttpClient.Builder().addInterceptor(interceptor).build()
             */

            retrofit = Retrofit.Builder()
                .baseUrl("http://192.249.18.245:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }
    }
}

interface APIInterface {
    @FormUrlEncoded
    @POST("users/postman")
    fun createUser(@Field("userId") userId:String,@Field("name") name:String, @Field("phoneNum") userPhoneNum:String): Call<ResponseBody>

    @GET("users/{userPhoneNum}")
    fun getUserIdByPhoneNumber(@Path("userPhoneNum")userPhoneNum:String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("users/friends")
    fun getFriendUsers(@Field("userId")userId:String, @Field("phoneList")phoneList:ArrayList<String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("groups/postman")
    fun createGroup(@Field("groupName")groupName:String, @Field("groupId")groupId:String, @Field("participants")participants:ArrayList<String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("users/updategroup")
    fun updateUsersGroup(@Field("groupName")groupId:String, @Field("usersNames")participants:ArrayList<String>):Call<ResponseBody>

    @GET("users/getgrouplist/{Id}")
    fun getGroupListById(@Path("Id")userId:String):Call<ResponseBody>

    @GET("groups/members/{groupName}")
    fun getMembersByName(@Path("groupName") groupName:String):Call<ResponseBody>

    @GET("users/{phoneNum}")
    fun getNameByPH(@Path("phoneNum")phoneNum:String):Call<ResponseBody>

    @FormUrlEncoded
    @POST("events/postman")
    fun createEvent(@Field("name")name:String, @Field("date")date:String, @Field("members")members:ArrayList<String>, @Field("description")description:String):Call<ResponseBody>

    @FormUrlEncoded
    @POST("users/updateEvent")
    fun updateUsersEvent(@Field("eventName")groupId:String, @Field("usersNames")participants:ArrayList<String>):Call<ResponseBody>

    @GET("users/geteventlist/{id}")
    fun getEventList(@Path("id")id:String):Call<ResponseBody>

    @GET("events/oneEvent/{name}")
    fun getOneEvent(@Path("name")name:String):Call<ResponseBody>
}