package com.example.testapi

data class AnswerSubmission(
    val user_id: Int,
    val question_id: String,
    val selected_answer: String,
    val category_id: Int
)
