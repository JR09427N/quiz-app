package com.example.testapi

import com.google.gson.annotations.SerializedName

data class SelectedAnswerResponse(
    @SerializedName("selected_answer") val selectedAnswer: String?
)