package com.cpp.inonews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_remote_keys")
data class ArticleRemoteKeys(
    @PrimaryKey val url: String,
    val prevKey: Int?,
    val nextKey: Int?
)
