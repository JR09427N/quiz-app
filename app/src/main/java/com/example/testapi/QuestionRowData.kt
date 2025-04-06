package com.example.testapi

import com.google.gson.annotations.SerializedName

data class QuestionRowData(
    val ID: String?,
    val Category: String?,
    val Question: String?,
    val Format: String?,
    @SerializedName("Number of Answers") val numberOfAnswers: Int?,
    @SerializedName("Answer Options") val answerOptions: String?,
    @SerializedName("Matching Weight") val matchingWeight: Double?,
    @SerializedName("Personality Type") val personalityType: String?,
    @SerializedName("Reverse Scoring") val reverseScoring: String?,
)
