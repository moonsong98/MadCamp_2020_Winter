package com.example.myapplication

data class ContactData (val id: Int, val name: String, val food_type: String, val restaurant_type: Int, val phone_number: String, val location: String)
// If restaurant type is 1, it's delivery. Otherwise, it's visit