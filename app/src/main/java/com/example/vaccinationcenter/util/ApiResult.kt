package com.example.vaccinationcenter.util
// API 깔끔하게 사용하기위한 util
sealed class ApiResult<out T : Any?>

data class Success<out T : Any?>(val data: T) : ApiResult<T>()

data class ApiError(val exception: Exception) : ApiResult<Nothing>()

data class ExceptionError(val exception: Exception) : ApiResult<Nothing>()