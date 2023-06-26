package com.chutyrooms.mvvmnews.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.*
import com.chutyrooms.mvvmnews.NewsApplication
import com.chutyrooms.mvvmnews.models.Article
import com.chutyrooms.mvvmnews.models.NewsResponse
import com.chutyrooms.mvvmnews.repository.NewsRepository
import com.chutyrooms.mvvmnews.utils.NetworkUtils.Companion.isInternetAvailable
import com.chutyrooms.mvvmnews.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query
import java.io.IOException

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {


    init{

        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.safeBreakingNewsCall("us")
        }
    }



    val breakingNews: LiveData<Resource<NewsResponse>>
    get()=newsRepository.showBreakingNews

    val searchNews: LiveData<Resource<NewsResponse>>
        get()=newsRepository.showSearchNews

    val searchNewsPage=newsRepository.searchNewsPage
    val breakingNewsPage=newsRepository.breakingNewsPage


    fun searchNews(searchQuery: String) = viewModelScope.launch {
        newsRepository.safeSearchNewsCall(searchQuery)
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {

       newsRepository.safeBreakingNewsCall(countryCode)

    }




    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepository.uspert(article)
    }

    fun getSavedNews()= newsRepository.getSavedNews()

    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }





}