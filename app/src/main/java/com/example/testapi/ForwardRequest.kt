package com.example.testapi

data class ForwardRequest(
    val sender_id: Int,
    val receiver_id: Int,
    val question_id: String
)

