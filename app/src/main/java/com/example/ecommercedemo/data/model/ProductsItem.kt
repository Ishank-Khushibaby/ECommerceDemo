package com.example.ecommercedemo.data.model

import com.google.gson.annotations.SerializedName

data class ProductsItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Int
)