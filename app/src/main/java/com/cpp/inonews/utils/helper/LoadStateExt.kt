package com.cpp.inonews.utils.helper

import androidx.paging.LoadState
import com.cpp.inonews.data.remote.Result

fun LoadState.asResult(): Result<Unit> = when (this) {
    is LoadState.Loading -> Result.Loading
    is LoadState.Error -> Result.Error(
        this.error.message ?: "Unknown error",
        throwable = this.error
    )
    is LoadState.NotLoading -> Result.Success(Unit)
}