package com.example.ecommercedemo.data.repository

import com.example.ecommercedemo.data.utils.Envelope
import com.example.ecommercedemo.data.model.ProductsItem
import kotlinx.coroutines.flow.Flow

interface ProductsRepository{
    fun getProducts(): Flow<Envelope<List<ProductsItem>>>
}
