package com.example.newsapp_2.api

import com.example.newsapp_2.models.NewsResponse
import com.example.newsapp_2.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String = "us",
        @Query("apiKey")
        apiKey:String = Constants.API_KEY
    ) : Response<NewsResponse>

    @GET("top-headlines")
    suspend fun searchNews(
        @Query("q")
        searchQuery:String,
        @Query("apiKey")
        apiKey:String = Constants.API_KEY
    ) : Response<NewsResponse>
}