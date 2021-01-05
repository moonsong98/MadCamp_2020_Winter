package com.example.myapplication

data class VisitReviewData(
    var restaurant: String, var rating: Int, var review: String,
    val timeStamp: String
)