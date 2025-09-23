package com.cpp.inonews.data.mapper

import com.cpp.inonews.data.local.entity.ArticleEntity
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem

fun ArticlesItem.toEntity(): ArticleEntity {
    return ArticleEntity(
        url = this.url ?: "",
        publishedAt = this.publishedAt,
        author = this.author,
        urlToImage = this.urlToImage,
        description = this.description,
        title = this.title,
        content = this.content,
        sourceId = this.source?.id,
        sourceName = this.source?.name
    )
}