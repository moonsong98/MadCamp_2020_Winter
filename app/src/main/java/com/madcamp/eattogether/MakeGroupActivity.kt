package com.madcamp.eattogether

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.Profile
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeGroupActivity: AppCompatActivity() {
    private lateinit var peopleList: RecyclerView
    private lateinit var groupPeopleAdapter: GroupPeopleAdapter
    private lateinit var createBtn : Button
    var groupPeople : ArrayList<Phone> = ArrayList()
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_make_group)
        peopleList = findViewById(R.id.toaddList)
        var intent: Intent = intent
        var friendList : ArrayList<Phone> = intent.getSerializableExtra("friendList") as ArrayList<Phone>
        Log.i("aaaaaaa",friendList.size.toString())
        groupPeopleAdapter = GroupPeopleAdapter(this, friendList)
        peopleList.adapter = groupPeopleAdapter
        peopleList.layoutManager = LinearLayoutManager(this)
        createBtn = findViewById(R.id.create)
        createBtn.setOnClickListener {
            var i : Int = 0
            while(i<friendList.size){
                if(friendList[i].ifadded){
                    groupPeople.add(friendList[i])
                    Log.i("aaaaaa",friendList[i].toString())
                }
                i=i+1
                Log.i("aaaaaa",groupPeople.toString())
            }
            val phoneNumbsersOfparticipants = groupPeople.map{
                it.phoneNumber
            } as ArrayList<String>
            val groupId = Profile.getCurrentProfile().id+"group1"
            Log.i("aaaaaa",groupId)
            val apiInterface = APIClient.getClient().create(APIInterface::class.java)
            val call:Call<ResponseBody> = apiInterface.createGroup(groupId, phoneNumbsersOfparticipants)
            call.enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    Toast.makeText(this@MakeGroupActivity, "Succeeded to Create Group",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@MakeGroupActivity, MainActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                    this@MakeGroupActivity.finish()
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@MakeGroupActivity, "Fail to Create Group", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}