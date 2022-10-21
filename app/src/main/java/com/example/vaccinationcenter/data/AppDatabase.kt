package com.example.vaccinationcenter.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Center 데이터베이스
@Database(entities = [Center::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CenterDao(): CenterDao
}