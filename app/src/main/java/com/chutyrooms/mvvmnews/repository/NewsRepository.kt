package com.chutyrooms.mvvmnews.repository

import androidx.lifecycle.MutableLiveData
import com.chutyrooms.mvvmnews.api.RetrofitInstance
import com.chutyrooms.mvvmnews.db.ArticleDatabase
import com.chutyrooms.mvvmnews.models.NewsResponse
import com.chutyrooms.mvvmnews.utils.Resource

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String,pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)




}