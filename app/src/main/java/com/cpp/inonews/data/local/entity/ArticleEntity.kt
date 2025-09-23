package com.cpp.inonews.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val url: String = "",

    val publishedAt: String? = null,
    val author: String? = null,
    val urlToImage: String? = null,
    val description: String? = null,
    val title: String? = null,
    val content: String? = null,

    // Flatten Source
    val sourceId: String? = null,
    val sourceName: String? = null
): Parcelable
