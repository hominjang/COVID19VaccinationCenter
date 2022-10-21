package com.example.vaccinationcenter.util

import android.util.Log
import com.google.gson.JsonSyntaxException
import okio.IOException
import org.json.JSONObject
import retrofit2.Response
// API 깔끔하게 사용하기위한 util
suspend fun <T : Any> handleApi(
    call: suspend () -> Response<T>,
    errorMessage: String = "Some errors occurred, Please try again later"
): ApiResult<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
//            isConnectedToNetwork = true
            response.body()?.let {
                return Success(it)
            }
        }
        response.errorBody()?.let {
            try {
                Log.d("items", "여기?")
                val errorString  = it.string()
                val errorObject = JSONObject(errorString)
                return ApiError(
                    RuntimeException(if(errorObject.has("message")) errorObject.getString("message") else "Error occurred, Try again Later"))
            } catch (ignored: JsonSyntaxException) {
                Log.d("items", "json sytnax error")
                return ApiError(RuntimeException(errorMessage))
            }
        }
        return ApiError(RuntimeException(errorMessage))
    } catch (e: Exception) {
        if (e is IOException) {
//            isConnectedToNetwork = false
        }
        Log.d("items", " $e")
        return ApiError(RuntimeException(errorMessage))
    }
}