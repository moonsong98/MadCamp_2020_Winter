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
                .baseUrl("http://192.249.18.238:3002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }
    }
}

interface APIInterface {
    @FormUrlEncoded
    @POST("users/postman")
    fun createUser(@Field("userId") userId:String, @Field("userPhoneNum") userPhoneNum:String): Call<ResponseBody>

    @GET("users/{userPhoneNum}")
    fun getUserIdByPhoneNumber(@Path("userPhoneNum")userPhoneNum:String): Call<ResponseBody>
}