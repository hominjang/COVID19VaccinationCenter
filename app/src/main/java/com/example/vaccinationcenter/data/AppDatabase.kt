package com.example.vaccinationcenter.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Center::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CenterDao(): CenterDao
}