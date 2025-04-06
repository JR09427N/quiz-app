package com.example.testapi

import com.google.gson.annotations.SerializedName

data class ReadNameResponse(
    @SerializedName("read_name_from_id") val nameRead: String  // Map JSON field "read_name_from_id" to "nameRead"
)