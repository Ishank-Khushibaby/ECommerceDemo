package com.example.ecommercedemo.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercedemo.data.model.ProductsItem
import com.example.ecommercedemo.data.repository.ProductsRepository
import com.example.ecommercedemo.data.utils.Envelope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    private val _state = mutableStateOf(HomeContract.HomeState())
    val state: State<HomeContract.HomeState> = _state

    private val _effect = MutableSharedFlow<HomeContract.Effect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: HomeContract.HomeEvent) {
        when (event) {
            is HomeContract.HomeEvent.AddProductToBucket -> addItemToBucket(event.item)
        }
    }

//    private fun addItemToBucket(item: ProductsItem) {
//        val list = state.value.selectedProducts.toMutableList()
//        list.add(item)
//        _state.value = state.value.copy(
//            selectedProducts = list
//        )
//        viewModelScope.launch {
//            _effect.emit(
//                HomeContract.Effect.OnApiError(
//                    message = "${item.name} added to Basket !"
//                )
//            )
//        }
//    }

    private fun addItemToBucket(item: ProductsItem) {
        _state.value = state.value.copy(
            selectedProducts = state.value.selectedProducts + item
        )

        viewModelScope.launch {
            _effect.emit(
                HomeContract.Effect.OnApiError(
                    message = "${item.name} added to Basket!"
                )
            )
        }
    }


    fun getProducts() {
        productsRepository.getProducts().map {
            when (it) {
                is Envelope.Error -> {
                    _state.value = state.value.copy(
                        isLoading = false
                    )
                    _effect.emit(
                        HomeContract.Effect.OnApiError(
                            message = it.error.message.orEmpty()
                        )
                    )
                }

                is Envelope.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }

                is Envelope.Success -> {
                    _state.value = state.value.copy(isLoading = false,
                        products = it.data,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}