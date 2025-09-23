package com.cpp.inonews.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cpp.inonews.data.local.dao.ArticleDao
import com.cpp.inonews.data.local.dao.ArticleRemoteKeysDao
import com.cpp.inonews.data.local.entity.ArticleEntity
import com.cpp.inonews.data.local.entity.ArticleRemoteKeys

@Database(
    entities = [ArticleEntity::class, ArticleRemoteKeys::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeysDao(): ArticleRemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}