package com.example.myapplication.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingDto(val rate: Double, val count: Int)
