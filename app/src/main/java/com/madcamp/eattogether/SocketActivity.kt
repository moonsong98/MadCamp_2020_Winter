package com.madcamp.eattogether

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class SocketActivity: AppCompatActivity() {
    lateinit var mSocket : Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.socket_layout)
        try {
            mSocket = IO.socket("http://192.249.18.245:8080")
            Log.i("success",mSocket.id())
        } catch (e: URISyntaxException) {
            Log.e("SocketActivity", e.reason)
        }
        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect);


    }
    val onConnect : Emitter.Listener = Emitter.Listener {

    }
}