package com.example.newsapp_2.utils

/* inform the user about the state of information
    loading,
    sucess,
    error
 */

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T) : Resource<T>(data) // if it is success show data
    class Loading<T>(data: T?=null) : Resource<T>(data) // if it is Loading nothing to show
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message) // if it is an error, show error message
}