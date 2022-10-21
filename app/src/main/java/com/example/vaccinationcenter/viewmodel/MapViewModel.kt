package com.example.vaccinationcenter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.vaccinationcenter.data.Center
import com.example.vaccinationcenter.data.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

// repository의 인스턴스를 Hilt를 통해 넣어줍니다.

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: CenterRepository) : ViewModel() {

    // DB에 저장되어있는 데이터
    private val _centerList = MutableLiveData<List<Center>>()
    val centerList: LiveData<List<Center>>
        get() = _centerList

    // 버튼 클릭용 LiveData
    private val _btnClick = MutableLiveData<Int>()
    val btnClick: LiveData<Int>
        get() = _btnClick

    fun viewModelStart() {
        // map 구성이 다 완료되면, DB 데이터 가져오기
        viewModelScope.launch {
            repository.getAll().let {
                _centerList.postValue(it)
            }
        }
    }

    fun findMyLocation() {
        // view에서 현재 위치 불러오는 버튼 클릭하면 호출
        _btnClick.postValue(200)
    }

}