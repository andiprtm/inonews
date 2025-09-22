package com.cpp.inonews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val url: String, // biasanya unik di setiap artikel

    val publishedAt: String? = null,
    val author: String? = null,
    val urlToImage: String? = null,
    val description: String? = null,
    val title: String? = null,
    val content: String? = null,

    // Flatten Source
    val sourceId: String? = null,
    val sourceName: String? = null
)
