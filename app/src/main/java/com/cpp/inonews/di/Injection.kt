package com.cpp.inonews.di

import android.content.Context
import com.cpp.inonews.data.local.AppDatabase
import com.cpp.inonews.data.remote.retrofit.ApiConfig
import com.cpp.inonews.repository.NewsRepository

object Injection {
    fun provideNewsRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val appDatabase = AppDatabase.getDatabase(context)
        return NewsRepository.getInstance(appDatabase, apiService)
    }
}