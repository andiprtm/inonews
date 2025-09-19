package com.cpp.inonews.utils.helper.network

import com.cpp.inonews.data.remote.Result
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> {
                Result.Error("Check your internet connection")
            }
            is HttpException -> {
                val errorResponse = parseError(throwable)
                Result.Error("HTTP ${throwable.code()}: $errorResponse")
            }
            else -> {
                Result.Error("Unexpected error: ${throwable.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}

fun parseError(e: HttpException): String {
    return try {
        val errorBody = e.response()?.errorBody()?.string()
        if (!errorBody.isNullOrEmpty()) {
            val json = JSONObject(errorBody)
            json.optString("message")
                ?: json.optString("error")
                ?: json.optString("errors")
                ?: "Unknown error"
        } else {
            "Unknown error"
        }
    } catch (ex: Exception) {
        "Error parsing response: ${ex.message}"
    }
}