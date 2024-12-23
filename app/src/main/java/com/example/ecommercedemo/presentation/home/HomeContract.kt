package com.example.ecommercedemo.presentation.home

import com.example.ecommercedemo.data.model.ProductsItem

class HomeContract {
    sealed class HomeEvent {
        data class AddProductToBucket(val item: ProductsItem): HomeEvent()
    }

    data class HomeState(
        val isLoading: Boolean = false,
        val products: List<ProductsItem> = listOf(),
        val selectedProducts: List<ProductsItem> = listOf(),
        val totalAmount: Long = 0,
    )

    sealed class Effect {
        data class OnApiError(val message: String) : Effect()
        sealed class Navigation : Effect() {
            data object NavigateToBucket : Navigation()
            data object NavigateToBack : Navigation()
        }
    }
}