package com.cpp.inonews.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import com.cpp.inonews.data.remote.retrofitg.ApiService
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(private val apiService: ApiService, private val country: String) : PagingSource<Int, ArticlesItem>() {
    override fun getRefreshKey(state: PagingState<Int, ArticlesItem>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getTopHeadlinesNews(
                countryCode = country,
                page = position,
                size = params.loadSize
            )

            LoadResult.Page(
                data = responseData.articles,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.articles.isEmpty()) null else position + 1
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}