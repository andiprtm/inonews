package com.cpp.inonews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cpp.inonews.data.remote.retrofitg.ApiService
import retrofit2.HttpException
import com.cpp.inonews.data.remote.Result
import com.cpp.inonews.data.remote.paging.NewsPagingSource
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import com.cpp.inonews.data.remote.responses.topheadlines.TopHeadlinesResponse
import com.cpp.inonews.utils.helper.network.safeApiCall
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.json.JSONObject
import java.io.IOException

class NewsRepository (
    private val apiService: ApiService
) {

    fun getPageNews(
        country: String = "us",
        pageSize: Int = 5
    ): LiveData<Result<PagingData<ArticlesItem>>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(apiService, country)
            }
        ).flow
            .map<PagingData<ArticlesItem>, Result<PagingData<ArticlesItem>>> { pagingData ->
                Result.Success(pagingData)
            }
            .onStart { emit(Result.Loading) }
            .catch { e -> emit(Result.Error(e.message ?: "Unknown error", throwable = e)) }
            .asLiveData()
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