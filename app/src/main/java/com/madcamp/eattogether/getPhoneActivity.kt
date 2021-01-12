package com.madcamp.eattogether

import android.content.Context
import android.content.Intent
import android.os.AsyncTask.execute
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.Profile
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor

class getPhoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_getphone)

        val context = this@getPhoneActivity
        val sendInfo:Button = findViewById(R.id.sendPhoneNum)
//        val name = intent.getStringExtra("name")
//        Toast.makeText(this,"Hello"+name, LENGTH_SHORT)
        sendInfo.setOnClickListener {
            val userPhoneNum = findViewById<EditText>(R.id.userPhoneNum).text.toString()
            val userId = Profile.getCurrentProfile().id
            val name = Profile.getCurrentProfile().name
            Log.i("aaaaa",name)
            Log.i("aaaaaa",name)
            val profile = Profile.getCurrentProfile().getProfilePictureUri(100,100)
            val apiInterface = APIClient.getClient().create(APIInterface::class.java)
            val call:Call<ResponseBody> = apiInterface.createUser(userId, name, userPhoneNum)
            call.enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    Toast.makeText(this@getPhoneActivity, "Succeeded to Create User", LENGTH_SHORT).show()
                    val intent = Intent(this@getPhoneActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                    context.finish()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@getPhoneActivity, "Fail to Create User", LENGTH_SHORT).show()
                }
            })
        }
    }
}
