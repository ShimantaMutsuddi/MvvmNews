package com.chutyrooms.mvvmnews.ui

import androidx.lifecycle.ViewModel
import com.chutyrooms.mvvmnews.repository.NewsRepository

class NewsViewModel(val newsRepository : NewsRepository) : ViewModel() {
}