package com.example.newsapp_2.ui.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp_2.db.ArticleDatabase
import com.example.newsapp_2.repository.NewsRepository

class NewsViewModelFactory(
    val app:Application,
    private val repository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, repository) as T
    }
}