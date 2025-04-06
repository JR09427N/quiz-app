package com.example.testapi

import com.google.gson.annotations.SerializedName

data class AnswerCountResponse(
    @SerializedName("count") val count: Int
)