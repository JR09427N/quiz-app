package com.example.testapi

import com.google.gson.annotations.SerializedName

data class UserQuestionData(
    val question_id: String,
    val question_text: String,
    val question_options: String,
    val question_type: String,
    val category: Int,
    val created_at: String,
    val is_active: Boolean
)
