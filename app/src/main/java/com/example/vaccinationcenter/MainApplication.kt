package com.example.vaccinationcenter

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import com.example.vaccinationcenter.data.AppDatabase
import com.example.vaccinationcenter.data.Center
import com.example.vaccinationcenter.data.CenterRepository
import com.example.vaccinationcenter.data.InitApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainApplication : Application() {

    lateinit var initApplication: InitApplication


    override fun onCreate() {
        super.onCreate()
        initApplication = InitApplication(applicationContext)
    }

}

