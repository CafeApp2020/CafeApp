package com.cafeapp.mycafe.use_case.data

sealed class CategoryResult {
    data class Success<out T>(val data : T): CategoryResult()
    data class Error(val error : Throwable): CategoryResult()
}