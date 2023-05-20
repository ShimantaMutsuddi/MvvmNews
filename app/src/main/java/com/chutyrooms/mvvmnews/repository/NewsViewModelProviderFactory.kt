package com.chutyrooms.mvvmnews.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chutyrooms.mvvmnews.ui.NewsViewModel

class NewsViewModelProviderFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}