package com.example.event_retrofit.activity

sealed class NetworkResult<out R> {

    data class Success<out T : Any>(val output : T) : NetworkResult<T>()
    data class Error(val exception: Throwable)  : NetworkResult<Nothing>()

}


