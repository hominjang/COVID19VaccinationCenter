package com.example.vaccinationcenter.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.vaccinationcenter.data.AppDatabase
import com.example.vaccinationcenter.data.CenterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// DB 주입을 위해 구현
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        // DB 생성 및 반환
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "center.db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideCenterDao(database: AppDatabase): CenterDao {
        // DAO 반환
        return database.CenterDao()
    }
}