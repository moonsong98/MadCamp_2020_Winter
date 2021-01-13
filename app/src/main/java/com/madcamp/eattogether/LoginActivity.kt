package com.madcamp.eattogether

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.Login
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects.toString


class LoginActivity: AppCompatActivity() {
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private lateinit var login : LoginButton
    private lateinit var profile: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        profile = findViewById(R.id.profile)
        login = findViewById(R.id.login)

        /* Check Whether Login Session is still alive */
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        /* If login session is still alive, move to next main activity */
        if(isLoggedIn){
            Toast.makeText(this@LoginActivity, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
            val nextIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(nextIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        login.visibility = VISIBLE
        login.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = loginResult.accessToken

//                val name: String = Profile.getCurrentProfile().getLastName() + Profile.getCurrentProfile()
//                    .getFirstName() + Profile.getCurrentProfile().getMiddleName()
//                Log.i("aaaaaaaa",name)
//                Toast.makeText(this@LoginActivity,"Hello"+name, Toast.LENGTH_SHORT)

                /* Check Whether User Signed In */
                val userId = accessToken.userId
                val apiInterface = APIClient.getClient().create(APIInterface::class.java)
                apiInterface.getUserIdByPhoneNumber(userId).enqueue(object :
                    Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val error = -1
                        if (response.body() != null && response.body()!!
                                .string() != error.toString()
                        ) {
                            /* User Has Signed In Before */
                            Toast.makeText(
                                this@LoginActivity, response.body()!!.string(),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        } else {
                            /* User Hasn't Signed In Before - Move to Sign In Page  */
                            Toast.makeText(
                                this@LoginActivity, userId.toString() + "Failed to Find User",
                                Toast.LENGTH_SHORT
                            ).show()
                            val nextIntent = Intent(
                                this@LoginActivity,
                                getPhoneActivity::class.java
                            )
//                            nextIntent.putExtra("name", name)
                            startActivity(nextIntent)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(
                            this@LoginActivity, "Error to Find User",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

