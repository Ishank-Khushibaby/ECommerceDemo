package com.example.ecommercedemo.data.repository

import com.example.ecommercedemo.data.utils.Envelope
import com.example.ecommercedemo.data.model.ProductsItem
import com.example.ecommercedemo.data.service.ECommerceService
import com.example.ecommercedemo.data.utils.Utils.getErrorModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val eCommerceService: ECommerceService,
) : ProductsRepository {
    override fun getProducts(): Flow<Envelope<List<ProductsItem>>> = flow{
        emit(Envelope.loading())
        val response = eCommerceService.getProducts()
        if (response.isSuccessful && response.body() != null) {
            emit(Envelope.success(response.body()!!))
        } else {
            emit(Envelope.error(response.getErrorModel()))
        }
    }.catch { e ->
        emit(Envelope.error(e.getErrorModel()))
        e.printStackTrace()
    }

}