package com.example.vaccinationcenter.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.vaccinationcenter.BuildConfig
import com.example.vaccinationcenter.data.AppDatabase
import com.example.vaccinationcenter.data.CenterDao
import com.example.vaccinationcenter.data.RetroAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetroModule {

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext appContext: Context): Retrofit {
        // Retrofit 생성 및 반환
        val BASE_URL = "https://api.odcloud.kr/api/15077586/v1/"

        // 통신로그 용도
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

    @Provides
    fun provideRetroAPI(retrofit : Retrofit): RetroAPI {
        // RetroAPI 반환
        return retrofit.create(RetroAPI::class.java)
    }
}