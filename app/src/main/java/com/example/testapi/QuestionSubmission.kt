package com.example.testapi

data class QuestionSubmission(
    val user_id: Int,
    val question_type: String,
    val question_text: String,
    val question_options: String
)
