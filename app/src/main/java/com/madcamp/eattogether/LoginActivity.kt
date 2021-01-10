package com.madcamp.eattogether

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
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
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private lateinit var login : LoginButton
    private lateinit var profile: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        /* Check Whether Login Session is still alive */
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        /* If login session is still alive, move to next main activity */
        if(isLoggedIn){
            Toast.makeText(this@LoginActivity,"로그인에 성공하셨습니다.",Toast.LENGTH_SHORT).show()
            val nextIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(nextIntent)
        }
        else {
            profile = findViewById(R.id.profile)
            login = findViewById(R.id.login)
            login.visibility = VISIBLE
            // Callback registration
            login.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // App code
                    val imageUrl: String =
                        "https://graph.facebook.com/" + loginResult.accessToken.userId + "/picture?return_ssl_resources=1"
                    Picasso.get().load(imageUrl).into(profile)
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
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

