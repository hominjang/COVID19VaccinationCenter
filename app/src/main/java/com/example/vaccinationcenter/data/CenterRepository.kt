package com.example.vaccinationcenter.data

import javax.inject.Inject
import javax.inject.Singleton


// DatabaseModule / RetroitModule 에서 구현한 인스턴스들을 Hilt를 통해 넣어줍니다.
@Singleton
class CenterRepository @Inject constructor(
    private val databaseInstance : CenterDao,
    private val retroAPI: RetroAPI) {

    // DB 쿼리 삽입/다 가져오기
    fun getAll() = databaseInstance.getAll()
    fun insert(centerList: List<Center>) = databaseInstance.insert(centerList)

    // Retrofit 통신
    suspend fun getCenterList(page: Int) = retroAPI.getCenterList(page)
}