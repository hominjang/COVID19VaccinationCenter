package com.example.vaccinationcenter.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Center")
data class Center(
    // 데이터 출력용 변수 5개
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

    // 마커 색깔 다르게 하기위한 타입 변수
    @SerializedName("centerType")
    val centerType : String,

    // 마커 찍기 위한 좌표 데이터
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String
)
