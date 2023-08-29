package com.example.newsapp_2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp_2.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao() : ArticleDao

    companion object{
        @Volatile
        private var instance : ArticleDatabase? = null


       @Synchronized
       fun getInstance(context: Context) : ArticleDatabase{
           if (instance == null){
               instance = Room.databaseBuilder(
                   context,
                   ArticleDatabase::class.java,
                   "article_db.db"
               ).build()
           }
           return instance as ArticleDatabase
       }

    }

}