package com.cpp.inonews.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.cpp.inonews.data.local.entity.ArticleEntity
import com.cpp.inonews.data.remote.Result
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import com.cpp.inonews.repository.NewsRepository

class MainViewModel(private val newsRepository: NewsRepository): ViewModel() {
    fun getAllNews(): LiveData<Result<PagingData<ArticleEntity>>> {
        return newsRepository.getPageNews()
    }
}