package com.chutyrooms.mvvmnews.api

import com.chutyrooms.mvvmnews.utils.Constants.Companion.API_KEY
import com.chutyrooms.mvvmnews.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey: String= API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey: String= API_KEY
    ) : Response<NewsResponse>

}