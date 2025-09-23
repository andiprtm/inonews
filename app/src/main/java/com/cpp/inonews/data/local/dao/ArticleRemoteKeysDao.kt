package com.cpp.inonews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cpp.inonews.data.local.entity.ArticleRemoteKeys

@Dao
interface ArticleRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<ArticleRemoteKeys>)

    @Query("SELECT * FROM article_remote_keys WHERE url = :url")
    suspend fun remoteKeysUrl(url: String?): ArticleRemoteKeys?

    @Query("DELETE FROM article_remote_keys")
    suspend fun clearRemoteKeys()
}