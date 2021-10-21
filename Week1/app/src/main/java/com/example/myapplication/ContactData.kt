package com.example.myapplication

import java.io.Serializable

data class ContactData (val id: Int, val name: String, val food_type: String, val restaurant_type: Int, val phone_number: String, val location: Place) : Serializable
// If restaurant type is 1, it's delivery. Otherwise, it's visit