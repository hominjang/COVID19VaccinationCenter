package com.example.vaccinationcenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccinationcenter.data.Center
import com.example.vaccinationcenter.data.CenterRepository
import com.example.vaccinationcenter.data.CenterResponse
import com.example.vaccinationcenter.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: CenterRepository) : ViewModel() {

    var centerResponse = mutableListOf<CenterResponse>()
    var center = mutableListOf<Center>()

    private val _retroFinish = MutableLiveData<Int>()
    val retroFinish: LiveData<Int>
        get() = _retroFinish

    private val _dbSaveFinish = MutableLiveData<Int>()
    val dbSaveFinish: LiveData<Int>
        get() = _dbSaveFinish

    private val _centerList = MutableLiveData<List<Center>>()
    val centerList: LiveData<List<Center>>
        get() = _centerList

    private val _progressNumber = MutableLiveData<Int>()
    val progressNumber: LiveData<Int>
        get() = _progressNumber

    init {
        viewModelScope.launch {

            for (i in 0..10) {


                when (val result: ApiResult<CenterResponse> =
                    handleApi({ repository.getCenterList(i) })) {
                    is Success -> {
                        for (i in result.data.data) {
                            center.add(i)
                        }
                        centerResponse.add(result.data)

                    }
                    is ApiError -> {
                        Log.d("items", "Api 에러입니다. : " + result.exception)
                        break
                    }
                    is ExceptionError -> {
                        Log.d("items", "Exception 에러임")
                        break
                    }

                }
                if (i == 10) {
                    _retroFinish.postValue(200)
                }
            }

        }

        viewModelScope.launch {
            for (i in 0..80) {
                _progressNumber.postValue(i)
                delay(3)
            }
        }


    }

    fun dataSave() {
        viewModelScope.launch {
            repository.insert(center).let {
                for (i in 80..100) {
                    _progressNumber.postValue(i)
                }
                _dbSaveFinish.postValue(200)
            }


        }
    }


}