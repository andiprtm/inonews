package com.cpp.inonews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cpp.inonews.data.local.AppDatabase
import com.cpp.inonews.data.local.entity.ArticleEntity
import com.cpp.inonews.data.remote.retrofitg.ApiService
import retrofit2.HttpException
import com.cpp.inonews.data.remote.Result
import com.cpp.inonews.data.remote.paging.ArticleRemoteMediator
import com.cpp.inonews.data.remote.paging.NewsPagingSource
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import com.cpp.inonews.data.remote.responses.topheadlines.TopHeadlinesResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.json.JSONObject
import java.io.IOException

class NewsRepository (
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPageNews(): LiveData<Result<PagingData<ArticleEntity>>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = ArticleRemoteMediator(appDatabase,apiService),
            pagingSourceFactory = {
                appDatabase.articleDao().pagingSource()
            }
        ).flow
            .map<PagingData<ArticleEntity>, Result<PagingData<ArticleEntity>>> { pagingData ->
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
            appDatabase: AppDatabase,
            apiService: ApiService
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(appDatabase, apiService).also {
                    instance = it
                }
            }
    }
}