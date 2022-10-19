package com.example.vaccinationcenter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CenterDao {
    @Query("SELECT * FROM Center ORDER BY centerName DESC")
    fun getAll(): List<Center>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(center : List<Center>)
}