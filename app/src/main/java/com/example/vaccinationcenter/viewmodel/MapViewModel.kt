package com.example.vaccinationcenter.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.vaccinationcenter.MainApplication
import com.example.vaccinationcenter.data.Center
import com.example.vaccinationcenter.data.CenterRepository
import com.example.vaccinationcenter.data.CenterResponse
import com.example.vaccinationcenter.util.*
import kotlinx.coroutines.launch
import java.util.Calendar.getInstance

class MapViewModel(private val repository: CenterRepository) : ViewModel() {


    private val _centerList = MutableLiveData<List<Center>>()
    val centerList: LiveData<List<Center>>
        get() = _centerList

    private val _btnClick = MutableLiveData<Int>()
    val btnClick: LiveData<Int>
        get() = _btnClick

    private var isPermissionGranted: Boolean = false

    fun viewModelStart() {
        viewModelScope.launch {
            repository.getAll().let {
                _centerList.postValue(it)
                for (i in it) {
                    if (i.centerType != "지역") {
                        Log.d("items", "sd" + i.centerType)
                    }
                }
            }
        }
    }

    fun findMyLocation() {
        if (isPermissionGranted) {
            _btnClick.postValue(200)
        }
    }

    fun permissionGranted() {
        isPermissionGranted = true
    }

}