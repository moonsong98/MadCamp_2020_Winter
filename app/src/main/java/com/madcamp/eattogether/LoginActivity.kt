package com.madcamp.eattogether

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.squareup.picasso.Picasso
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class LoginActivity: AppCompatActivity() {
    var callbackManager = CallbackManager.Factory.create()
    private lateinit var info : TextView
    private lateinit var login : LoginButton
    private lateinit var profile: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        info = findViewById(R.id.info)
        profile = findViewById(R.id.profile)
        login = findViewById(R.id.login)
        // Callback registration
        login.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    info.setText("User Id" + loginResult.accessToken.userId)
                    var imageUrl: String =
                        "https://graph.facebook.com/" + loginResult.accessToken.userId + "/picture?return_ssl_resources=1"
                    Picasso.get().load(imageUrl).into(profile)
//                    var memberInfo = JSONObject()
//                    memberInfo.put("userId", loginResult.accessToken.userId.toString())
//                    memberInfo.put("userProfileUrl", imageUrl)
                    val nextIntent = Intent(this@LoginActivity, getPhoneActivity::class.java)
                    nextIntent.putExtra("userId",loginResult.accessToken.userId.toString())
                    Log.i("aaaa",loginResult.accessToken.userId.toString())
                    nextIntent.putExtra("profileUrl",imageUrl)
                    Log.i("aaaa",imageUrl)
                    startActivity(nextIntent)
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })

        val accessToken = AccessToken.getCurrentAccessToken()//현재 로그인되어있는지
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().retrieveLoginStatus(this, object: LoginStatusCallback{
            override fun onCompleted(accessToken:AccessToken) {
                if(isLoggedIn){
                    Toast.makeText(this@LoginActivity,"Logged in as",Toast.LENGTH_SHORT)
                    val nextIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(nextIntent)
                }
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
            }
            override fun onFailure() {
                // No access token could be retrieved for the user
            }
            override fun onError(exception: Exception?) {
                // An error occurred
            }
        })





    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

