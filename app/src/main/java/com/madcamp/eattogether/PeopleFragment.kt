package com.madcamp.eattogether

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.facebook.Profile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleFragment : Fragment() {
    private lateinit var contactList: RecyclerView
    private lateinit var groupList: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var phoneAdapter: PhoneAdapter
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var contactListData: ArrayList<Phone> = ArrayList()
    private lateinit var createGroup: Button
    private var friendListData: ArrayList<Phone> = ArrayList() // List of contacts who are using our service
    private var groupListData: ArrayList<String> = ArrayList() // List of users' group
    private var searchText = ""
    private lateinit var noti : Button
    private lateinit var myProfile : ImageView

    companion object {
//        val name = Profile.getCurrentProfile().name
//        val profile = Profile.getCurrentProfile().getProfilePictureUri(100,100)
        private const val checkRequestCode = 1
        private val permissions = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_people, container, false)
        noti = view.findViewById(R.id.noti)
        noti.setOnClickListener {

        }
        createGroup = view.findViewById(R.id.create)
        contactList = view.findViewById(R.id.contact_list)
        groupList = view.findViewById(R.id.group_list)
        searchBar = view.findViewById(R.id.search_bar)
        myProfile = view.findViewById(R.id.myProfile)
       
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            if (friendListData.size > 0) {
                getFriendList()
                getGroupList()
            }
            else
                swipeRefreshLayout.isRefreshing = false
        }
        createGroup.setOnClickListener {
            var intent: Intent = Intent(context, MakeGroupActivity::class.java)
            intent.putExtra("friendList", friendListData)
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissionAndStart()
    }

    private fun checkPermissionAndStart() {
        if (isPermitted()) {
            startProcess()
        } else {
            requestPermissions(permissions, checkRequestCode)
        }
    }

    private fun isPermitted(): Boolean {
        return permissions.all { perm ->
            context?.let {
                checkSelfPermission(
                    it, perm
                )
            } == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startProcess() {
        /* Set adapter for recycler view */
        getPhoneNumbersFromPhone()
        getFriendList()
        getGroupList()
        /* Set search listener */
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                val filteredFriendListData = friendListData.filter {
                    it.name.contains(
                        searchText,
                        true
                    )
                } as ArrayList<Phone>
                phoneAdapter.setData(filteredFriendListData)
                phoneAdapter.notifyDataSetChanged()
                val filteredGroupListData = groupListData.filter {
                    it.contains(
                        searchText,
                        true
                    )
                }
                groupAdapter.setData(filteredGroupListData)
                groupAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun getPhoneNumbersFromPhone(): ArrayList<Phone> {
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projections = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " asc"
        val cursor = activity?.contentResolver?.query(
            phoneUri,
            projections,
            null,
            null,
            optionSort
        )

        if (cursor != null)
            while (cursor.moveToNext()) {
                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val number = cursor.getString(2)
                val phone = Phone(id, name, number)
                contactListData.add(phone)
            }
        return contactListData
    }

    private fun getFriendList() {
        val context = this.context
        val phoneNumbersOfContactListData = contactListData.map {
            it.phoneNumber
        } as ArrayList<String>
        val userId = Profile.getCurrentProfile().id
        val name = Profile.getCurrentProfile().name
        val profile = Profile.getCurrentProfile().getProfilePictureUri(100,100)
        Log.i("aaaaaaaa",name)
        Log.i("aaaaaaa",profile.toString())
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val serviceUserList = ArrayList<ServiceUser>()
        val serviceUserListPhone = ArrayList<String>()
        apiInterface.getFriendUsers(userId, phoneNumbersOfContactListData)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    response.body()?.let {
                        Toast.makeText(context, "Succeeded to get Response", LENGTH_SHORT).show()
                        val jsonArray = JSONArray(it.string())
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)

                            val tmp = ServiceUser(jsonObject.getString("name").toString(), jsonObject.getString("phoneNum").toString())
                            serviceUserList.add(tmp)
                            serviceUserListPhone.add(jsonObject.getString("phoneNum").toString())
                            Log.d("serviceUserList", jsonObject.getString("phoneNum"))
                        }
                    }
                    Log.d("serviceUserListSize:", serviceUserList.size.toString())
                    friendListData = contactListData.filter {
                        it.phoneNumber in serviceUserListPhone
                    } as ArrayList<Phone>

                    friendListData.map{ it ->
                        var phoneNumber = it.phoneNumber
                        it.name = (serviceUserList.find{e ->
                            e.phoneNumber == phoneNumber
                        })!!.name
                        Log.d("@@@@@@@@@@@@@@@@@", it.name)
                    }

                    phoneAdapter = PhoneAdapter(context!!, friendListData)
                    contactList.adapter = phoneAdapter
                    contactList.layoutManager = LinearLayoutManager(context)
                    Log.d("friendListSize:", friendListData.size.toString())
                    Toast.makeText(context, "Succeeded to get friend list", LENGTH_SHORT).show()
                    swipeRefreshLayout.isRefreshing = false
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context, "Failed to get friend list", LENGTH_SHORT).show()
                }
            })
    }

    private fun getGroupList() {
        val context = this.context // Debug
        val userId = Profile.getCurrentProfile().id
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        Toast.makeText(context, "ReachedHere", LENGTH_SHORT).show()
        apiInterface.getGroupListById(userId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(context, "Succeeded to get GroupList", LENGTH_SHORT).show()
                response.body()?.let {
                    groupListData = ArrayList()
                        val groupListFromJSON = JSONArray(it.string())
                        for (e in 0 until groupListFromJSON.length()) {
                            groupListData.add(groupListFromJSON[e].toString())
                        }
                    groupAdapter = GroupAdapter(context!!, groupListData)
                    groupList.adapter = groupAdapter
                    groupList.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Failed to get group list", LENGTH_SHORT).show()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            checkRequestCode -> {
                var valid = true
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED)
                        valid = false
                }
                if (valid) startProcess()
                else Toast.makeText(
                    context,
                    "연락처 권한 승인을 하셔야 앱을 사용하실 수 있습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    class PhoneAdapter(private val context: Context, private var list: List<Phone>) :
        RecyclerView.Adapter<PhoneAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.contact_list_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setView(list[position])
        }

        fun setData(newList: List<Phone>) {
            list = newList
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private lateinit var mPhone: Phone

            init {
                val callButton: ImageView = itemView.findViewById(R.id.contact_call_button)
                callButton.isClickable = true
                callButton.setOnClickListener {
                    mPhone.phoneNumber.let { phoneNumber ->
                        val uri = Uri.parse("tel:${phoneNumber}")
                        val intent = Intent(Intent.ACTION_CALL, uri)
                        itemView.context.startActivity(intent)
                    }
                }
            }

            fun setView(phone: Phone) {
                this.mPhone = phone
                val contactName: TextView = itemView.findViewById(R.id.contact_name)
                val contactNumber: TextView = itemView.findViewById(R.id.contact_phone_number)
                contactName.text = phone.name
                contactNumber.text = phone.phoneNumber
            }
        }
    }
    class GroupAdapter(private val context: Context, private var list: List<String>) :
        RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                context,
                LayoutInflater.from(context)
                    .inflate(R.layout.group_list_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setView(list[position])
        }

        fun setData(newList: List<String>) {
            list = newList
        }

        class ViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun setView(groupName:String) {
                val groupNameView:TextView = itemView.findViewById(R.id.group_name)
                val addAppointmentButton:ImageView = itemView.findViewById(R.id.add_appointment_button)
                groupNameView.text = groupName
                addAppointmentButton.isClickable = true
                addAppointmentButton.setOnClickListener{
                    val newActivity = Intent(context, AddAppointment::class.java)
                    newActivity.putExtra("groupName",groupName)
                    startActivity(context, newActivity, null)
                }
            }
        }
    }
    data class ServiceUser(val name: String, val phoneNumber:String)
}