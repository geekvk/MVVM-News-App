package com.example.newsapp_2.utils

import android.app.Application
import com.example.newsapp_2.db.ArticleDatabase

class MyApplication : Application() {
    val database by lazy {
        ArticleDatabase.getInstance(this)
    }
}