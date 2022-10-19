package com.example.vaccinationcenter.data

import com.google.gson.annotations.SerializedName

data class CenterResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("perPage")
    val perPage: Int,
    @SerializedName("matchCount")
    val matchCount: Int,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("currentCount")
    val currentCount: Int,
    @SerializedName("data")
    val data: List<Center>
)