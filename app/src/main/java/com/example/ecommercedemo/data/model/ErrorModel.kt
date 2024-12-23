package com.example.ecommercedemo.data.model

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("mode")
    var mode: Int? = null,
    @SerializedName("error_code")
    var errorCode: Int = 0,
)