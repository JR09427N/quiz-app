package com.example.testapi

import com.google.gson.annotations.SerializedName

data class IsMatchResponse(
    @SerializedName("is_same_face") val isMatch: Boolean  // Map JSON field "is_same_face" to "isMatch"
)