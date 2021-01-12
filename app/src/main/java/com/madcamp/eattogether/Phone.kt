package com.madcamp.eattogether

import java.io.Serializable

data class Phone (val id: String, val name: String, val phoneNumber: String, var ifadded : Boolean = false):
    Serializable