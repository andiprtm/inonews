package com.cpp.inonews.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.cpp.inonews.data.local.AppDatabase
import com.cpp.inonews.data.local.entity.ArticleEntity
import com.cpp.inonews.data.mapper.toEntity
import com.cpp.inonews.data.remote.retrofitg.ApiService

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val database: AppDatabase,
    private val apiService: ApiService
): RemoteMediator<Int, ArticleEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX

        try {
            val responseData = apiService.getTopHeadlinesNews(countryCode = "us", page, state.config.pageSize)

            val articles = responseData.articles.map { it.toEntity() }

            val endOfPaginationReached = responseData.articles.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.articleDao().clearAll()
                }
                database.articleDao().insertAll(articles)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception){
            return MediatorResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}