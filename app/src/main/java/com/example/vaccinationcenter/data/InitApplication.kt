package com.example.vaccinationcenter.data

import android.content.Context
import androidx.room.Room
import com.example.vaccinationcenter.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class InitApplication(applicationContext: Context) {

    val centerDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "center.db"
    ).allowMainThreadQueries()
        .build()

    val centerRetrofit: RetroAPI = initRetrofitBuilder().create(RetroAPI::class.java)

    private fun initRetrofitBuilder(): Retrofit {
        val BASE_URL = "https://api.odcloud.kr/api/15077586/v1/"

        // 로그용도
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(interceptor)
            .build()

        //리턴하는 레트로핏 빌더 반환
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}