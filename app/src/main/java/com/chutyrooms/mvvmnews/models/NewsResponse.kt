package com.chutyrooms.mvvmnews.models

import com.chutyrooms.mvvmnews.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)