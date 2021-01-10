package com.madcamp.eattogether

data class Appointment(val location:String, val Time: String, val attendants:ArrayList<String>, val waitResponseList:ArrayList<String>, val complete: Boolean)