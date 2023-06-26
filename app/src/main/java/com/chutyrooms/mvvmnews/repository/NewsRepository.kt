package com.chutyrooms.mvvmnews.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chutyrooms.mvvmnews.api.RetrofitInstance
import com.chutyrooms.mvvmnews.db.ArticleDatabase
import com.chutyrooms.mvvmnews.models.Article
import com.chutyrooms.mvvmnews.models.NewsResponse
import com.chutyrooms.mvvmnews.utils.NetworkUtils
import com.chutyrooms.mvvmnews.utils.Resource
import retrofit2.Response
import java.io.IOException

class NewsRepository(val db: ArticleDatabase,val application: Application) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse: NewsResponse ?=null
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse: NewsResponse?=null
    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null
    val showBreakingNews: LiveData<Resource<NewsResponse>>
    get()=breakingNews
    val showSearchNews: LiveData<Resource<NewsResponse>>
        get()=searchNews


    /*suspend fun getBreakingNews(countryCode: String,pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)



    suspend fun searchNews(searchQuery: String,pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)*/

    suspend fun uspert(article: Article)=db.getArticleDao().upSert(article)

    fun getSavedNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)

     suspend fun safeSearchNewsCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if(NetworkUtils.isInternetAvailable(application)) {
                val response =  RetrofitInstance.api.searchForNews(searchQuery, searchNewsPage)

                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if(NetworkUtils.isInternetAvailable(application)) {
                val response = RetrofitInstance.api.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if(response.isSuccessful)
        {

            response.body()?.let  {resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse== null)
                {
                    breakingNewsResponse=resultResponse
                }
                else{
                    val oldArticles= breakingNewsResponse?.articles
                    val newArticles=resultResponse.articles

                    oldArticles?.addAll(newArticles!!)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if(response.isSuccessful)
        {
            response.body()?.let  {resultResponse ->
                searchNewsPage++
                if(searchNewsResponse== null)
                {
                    searchNewsResponse=resultResponse
                }
                else{
                    val oldArticles= searchNewsResponse?.articles
                    val newArticles=resultResponse.articles

                    oldArticles?.addAll(newArticles!!)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}