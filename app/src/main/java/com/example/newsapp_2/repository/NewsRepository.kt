package com.example.newsapp_2.repository

import com.example.newsapp_2.api.RetrofitInstance
import com.example.newsapp_2.db.ArticleDatabase
import com.example.newsapp_2.models.Article

class NewsRepository(
    val db:ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode:String) = RetrofitInstance.api.getBreakingNews(countryCode)
    suspend fun searchNews(searchQuery:String) = RetrofitInstance.api.searchNews(searchQuery)

    suspend fun upsertNews(article: Article) = db.getArticleDao().upsertArtice(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}