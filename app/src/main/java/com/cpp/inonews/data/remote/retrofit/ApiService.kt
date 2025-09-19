package com.cpp.inonews.data.remote.retrofitg

import com.cpp.inonews.data.remote.responses.topheadlines.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    /* TOP HEADLINES */

    @GET("top-headlines")
    suspend fun getTopHeadlinesNews(
        @Query("country") countryCode: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") size: Int = 5
    ): TopHeadlinesResponse

}