package com.example.newsapp_2.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.newsapp_2.R
import com.example.newsapp_2.db.ArticleDatabase
import com.example.newsapp_2.repository.NewsRepository
import com.example.newsapp_2.ui.viewModel.NewsViewModel
import com.example.newsapp_2.ui.viewModel.NewsViewModelFactory
import com.example.newsapp_2.utils.MyApplication
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val viewModel : NewsViewModel by lazy {
        val database = ArticleDatabase.getInstance(this)
        val repository = NewsRepository(database)
        val factory = NewsViewModelFactory(application,repository)
        ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navController = Navigation.findNavController(this, R.id.hostFragment)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNav)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}