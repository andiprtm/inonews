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
import com.cpp.inonews.data.remote.paging.ArticleRemoteMediator

class NewsRepository (
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPageNews(): LiveData<PagingData<ArticleEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = ArticleRemoteMediator(appDatabase,apiService),
            pagingSourceFactory = {
                appDatabase.articleDao().pagingSource()
            }
        ).flow
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