package com.madcamp.eattogether

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class PeopleFragment : Fragment() {
    private lateinit var contactList: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var phoneAdapter: PhoneAdapter
    private var contactListData: ArrayList<Phone> = ArrayList()
    private var searchText = ""

    companion object {
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
        contactList = view.findViewById(R.id.contact_list)
        searchBar = view.findViewById(R.id.search_bar)
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
        phoneAdapter = PhoneAdapter(this.requireContext(), getPhoneNumbersFromPhone())
        contactList.adapter = phoneAdapter
        contactList.layoutManager = LinearLayoutManager(context)

        /* Set search listener */
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                val filteredContactListData = contactListData.filter {
                    it.name.contains(
                        searchText,
                        true
                    )
                } as ArrayList<Phone>
                phoneAdapter.setData(filteredContactListData)
                phoneAdapter.notifyDataSetChanged()
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
            while (cursor?.moveToNext() == true) {
                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val number = cursor.getString(2)
                val phone = Phone(id, name, number)
                contactListData.add(phone)
            }
        return contactListData
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

        fun setData(newList:List<Phone>) {
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
}