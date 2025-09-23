package com.cpp.inonews.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cpp.inonews.data.local.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM articles")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getCount(): Int

    @Query("""
        SELECT url, COUNT(url) as total
        FROM articles
        GROUP BY url
        HAVING COUNT(url) > 1
    """)
    suspend fun getDuplicates(): List<DuplicateCheck>
}

data class DuplicateCheck(
    val url: String,
    val total: Int
)