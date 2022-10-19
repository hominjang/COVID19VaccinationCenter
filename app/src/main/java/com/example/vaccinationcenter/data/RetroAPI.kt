package com.example.vaccinationcenter.data

import retrofit2.Response
import retrofit2.http.*
import java.net.URLDecoder

interface RetroAPI {

    companion object {
        private const val authKey = "bNmSjmL3NWL%2FmAmsQV0SyDT%2B8DCdZckhVg5%2FtSsmJHa47eBZBE%2BaFvCHYxeM1Dsz2FcgQ64elqYL3mr6GUyjOg%3D%3D"
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