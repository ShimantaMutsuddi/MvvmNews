package com.chutyrooms.mvvmnews.models

import com.chutyrooms.mvvmnews.models.Article

data class NewsResponse(
    var articles: MutableList<Article>?,
    var status: String?,
    var totalResults: Int?
)