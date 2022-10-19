package com.example.vaccinationcenter.data

import com.example.vaccinationcenter.MainApplication

class CenterRepository (application: MainApplication){

    private val databaseInstance = application.initApplication.centerDatabase.CenterDao()
    private val retroInstance = application.initApplication.centerRetrofit

    fun getAll() = databaseInstance.getAll()
    fun insert(centerList: List<Center>) = databaseInstance.insert(centerList)

    suspend fun getCenterList(page : Int) = retroInstance.getCenterList(page)
}