package com.madcamp.eattogether

import android.content.Context
import android.content.Intent
import android.os.AsyncTask.execute
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor

class getPhoneActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_getphone)
        var intent = intent
        var userId = intent.getStringExtra("userId")
        var profileUrl = intent.getStringExtra("profileUrl")
        val url = "http://192.249.18.245:8080/"
        var context: Context = this@getPhoneActivity
        var sendInfo = findViewById<Button>(R.id.sendPhoneNum)
        sendInfo.setOnClickListener {
            var userPhoneNum = findViewById<EditText>(R.id.userPhoneNum).text.toString()
            val queue = Volley.newRequestQueue(context)
            var userInfo = JSONObject()
            userInfo.put("userId",userId.toString())
            userInfo.put("userPhoneNum",userPhoneNum)
            Log.i("aaaaaa",userId.toString())
            Log.i("aaaaa",userPhoneNum)
            var jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, userInfo, Response.Listener{ },Response.ErrorListener (){})
            val intent = Intent(this@getPhoneActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
