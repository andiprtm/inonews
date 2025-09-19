package com.cpp.inonews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cpp.inonews.data.remote.retrofitg.ApiService
import retrofit2.HttpException
import com.cpp.inonews.data.remote.Result
import com.cpp.inonews.data.remote.responses.topheadlines.TopHeadlinesResponse
import com.cpp.inonews.utils.helper.network.safeApiCall
import org.json.JSONObject
import java.io.IOException

class NewsRepository (
    private val apiService: ApiService
) {

    fun getAllNews(
        country: String = "us",
        page: Int = 1,
        pageSize: Int = 20
    ): LiveData<Result<TopHeadlinesResponse>> = liveData {
        emit(Result.Loading)
        val result = safeApiCall {
            apiService.getTopHeadlinesNews(
                countryCode = country,
                page = page,
                size = pageSize
            )
        }
        emit(result)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(
            apiService: ApiService
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService).also {
                    instance = it
                }
            }
    }
}