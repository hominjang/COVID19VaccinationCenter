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


    fun viewModelStart() {
        viewModelScope.launch {
            repository.getAll().let {
                _centerList.postValue(it)
                Log.d("items", "sd" + it[0])
            }
        }
    }

}