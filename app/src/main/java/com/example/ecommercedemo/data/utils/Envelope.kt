package com.example.ecommercedemo.data.utils

import com.example.ecommercedemo.data.model.ErrorModel


sealed class Envelope<T> {

    class Loading<T> : Envelope<T>()
    data class Success<T>(val data: T) : Envelope<T>()
    data class Error<T>(val error: ErrorModel) : Envelope<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> error(error: ErrorModel) = Error<T>(error)
    }

}