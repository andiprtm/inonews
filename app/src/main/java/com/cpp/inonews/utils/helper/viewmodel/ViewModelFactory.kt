package com.cpp.inonews.utils.helper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cpp.inonews.di.Injection
import com.cpp.inonews.repository.NewsRepository
import com.cpp.inonews.ui.MainViewModel

class ViewModelFactory private constructor(
    private val newsRepository: NewsRepository
): ViewModelProvider.NewInstanceFactory() {

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: ViewModelFactory(
                    Injection.provideNewsRepository(context)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass){
            MainViewModel::class.java -> MainViewModel(newsRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}