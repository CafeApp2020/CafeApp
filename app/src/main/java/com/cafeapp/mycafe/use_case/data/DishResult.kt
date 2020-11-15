package com.cafeapp.mycafe.use_case.data


sealed class DishResult {
    data class Success<out T>(val data : T): DishResult()
    data class Error(val error : Throwable): DishResult()
}