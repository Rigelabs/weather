package com.example.weather.api

//T refers to the Weather Model
sealed class NetworkResponse<out T> {
    data class Success<out T>(val data : T) : NetworkResponse<T>()
    data class Error(val message:String) :NetworkResponse<Nothing>()
    object Loading:NetworkResponse<Nothing>()
}