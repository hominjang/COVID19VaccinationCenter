package com.example.vaccinationcenter.data

import retrofit2.Response
import retrofit2.http.*
import java.net.URLDecoder

interface RetroAPI {

    companion object {
        // 인증키 입니다.
        private const val authKey = "인증키 입니다!"
    }

    @GET("centers")
    suspend fun getCenterList(
        @Query("page")
        page: Int = 1,
        @Query("perPage")
        perPage: Int = 10,
        @Query("serviceKey")
        serviceKey: String = URLDecoder.decode(authKey, "UTF-8")
    ): Response<CenterResponse>

}