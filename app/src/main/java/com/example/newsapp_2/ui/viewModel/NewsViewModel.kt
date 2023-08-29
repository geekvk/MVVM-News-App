package com.example.newsapp_2.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp_2.NewsApplication
import com.example.newsapp_2.db.ArticleDatabase
import com.example.newsapp_2.models.Article
import com.example.newsapp_2.models.NewsResponse
import com.example.newsapp_2.repository.NewsRepository
import com.example.newsapp_2.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val repository: NewsRepository
) : AndroidViewModel(app) {
    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews : LiveData<Resource<NewsResponse>>
        get() { return _breakingNews }

    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews : LiveData<Resource<NewsResponse>>
        get() { return _searchNews }

    init {
        getBreakingNews("us")
    }

    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse ?= null

    var searchPage = 1
    val searchNewsResponse : NewsResponse ?= null

    private fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
//        val response = repository.getBreakingNews(countryCode)
//        _breakingNews.postValue(handleBreakingNewsRes(response))
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(q:String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
//        val response = repository.searchNews(q)
//        _searchNews.postValue(handleSearchNews(response))
        safeSearchNewsCall(q)
    }

    private fun handleBreakingNewsRes(response : Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {result ->
                breakingNewsPage++
                if (breakingNewsResponse == null){
                    breakingNewsResponse = result
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = result.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: result)
            }
        }
        return Resource.Error(null,response.message())
    }

    private fun handleSearchNews(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {res->

                return Resource.Success(res)
            }
        }
        return Resource.Error(null, response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsertNews(article)
    }

    fun getSavedNews() = repository.getSavedNews()

    fun deleteSavedNews(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(q: String){
        _searchNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = repository.searchNews(q)
                _searchNews.postValue(handleSearchNews(response))

            }else{
                _searchNews.postValue(Resource.Error(null, "No Internet connection"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException -> _searchNews.postValue(Resource.Error(null, "Network Failure"))
                else -> _searchNews.postValue(Resource.Error(null, "Conversion Error"))
            }

        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        _breakingNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = repository.getBreakingNews(countryCode)
                _breakingNews.postValue(handleBreakingNewsRes(response))

            }else{
                _breakingNews.postValue(Resource.Error(null, "No Internet connection"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException -> _breakingNews.postValue(Resource.Error(null, "Network Failure"))
                else -> _breakingNews.postValue(Resource.Error(null, "Conversion Error"))
            }

        }
    }

    private fun hasInternetConnection() : Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capability = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_CELLULAR) -> true
                capability.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    TYPE_WIFI ->true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}