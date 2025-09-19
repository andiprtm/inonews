package com.cpp.inonews.data.remote

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val throwable: Throwable? = null
    ) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}