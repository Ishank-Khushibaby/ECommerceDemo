package com.example.ecommercedemo.data.utils

import com.example.ecommercedemo.data.model.ErrorModel
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object Utils {

    fun <T> Response<T>.getErrorModel(): ErrorModel {
        return ErrorModel(
            message = getMessageFromMessage(),
            errorCode = code()
        )
    }


    private fun <T> Response<T>.getMessageFromMessage(): String {
        return try {
            val jObjError = JSONObject(errorBody()!!.string())
            var message = jObjError.getString("message")
            if (message.isEmpty()) {
                message = message()
            }
            message
        } catch (e: Exception) {
            getMessageByException(e)
        }
    }


    fun getMessageByException(
        t: Throwable,
    ): String {

        val messages = mapOf(
            SocketTimeoutException::class to "The connection has timed out. Please try again later.",
            ConnectException::class to "Failed to connect to the server. Please try again.",
            UnknownHostException::class to "Please check your internet connection.",
            IOException::class to "Please check your internet connection.",
            NullPointerException::class to "An unexpected error occurred. Please try again.",
            Throwable::class to "An unexpected error occurred. Please try again."
        )

        return messages[t::class] ?: messages[Throwable::class]!!
    }


    fun Throwable.getErrorModel(): ErrorModel {
        return ErrorModel(
            message = getMessageByException(this),
            errorCode = -1
        )
    }
}