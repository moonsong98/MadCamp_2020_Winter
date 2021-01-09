package com.madcamp.eattogether

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.squareup.picasso.Picasso
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
                    info.setText("User Id"+loginResult.accessToken.userId)
                    var imageUrl:String = "https://graph.facebook.com/"+loginResult.accessToken.userId +"/picture?return_ssl_resources=1"
                    Picasso.get().load(imageUrl).into(profile)
                    val nextIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(nextIntent)
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })

//        val accessToken = AccessToken.getCurrentAccessToken()
//        val isLoggedIn = accessToken != null && !accessToken.isExpired
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}