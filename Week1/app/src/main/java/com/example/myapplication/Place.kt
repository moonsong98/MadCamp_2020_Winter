package com.example.myapplication

import java.io.Serializable

data class Place(val address: String, val latitude: Double, val longitude: Double) : Serializable