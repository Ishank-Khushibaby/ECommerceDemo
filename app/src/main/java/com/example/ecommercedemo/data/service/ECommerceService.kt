package com.example.ecommercedemo.data.service

import com.example.ecommercedemo.data.model.ProductsItem
import retrofit2.Response
import retrofit2.http.GET

interface ECommerceService {
    @GET("products")
    suspend fun getProducts(): Response<List<ProductsItem>?>
}