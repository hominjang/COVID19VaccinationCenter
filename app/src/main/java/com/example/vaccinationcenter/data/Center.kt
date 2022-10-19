package com.example.vaccinationcenter.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Center")
data class Center(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("centerName")
    val centerName: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("facilityName")
    val facilityName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String
)
