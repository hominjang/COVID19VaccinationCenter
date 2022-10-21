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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// 데이터 통신 및 저장을 담당하는 SplashViewModel 입니다.
// 단계 진행용 LiveData 3개와 데이터 저장용 mutableList 2개로 구성하고있습니다.

// 데이터 통신 및 저장은 코루틴을 사용해 비동식으로 진행됩니다.
// 1. Retrofit으로 페이지를 비동기 데이터 통신
// 1-1) Success이면 데이터 추가
// 1-2) Fail 이면 통신 취소

// 2. 통신이 성공적이면, Livedata 통해서 데이터 저장 함수 호출 (Room)
// 2-1) 성공하면 Livedata 통해서 화면 전환 수행

// progressBar 를 제어하기 위해 LiveData / Job / Int 를 사용했습니다.
// 시작하면 코루틴을 활용하여 Int 변수를 제어합니다.(0 - 80(최대))
// 80에 도달하기 전에 작업이 끝나면, 코루틴 작업을 취소시키고, 저장된 Int 변수부터 숫자를 100까지 상승
// 80에 도달하고 작업이 끝나면, 멈춰있던 80부터 100까지 숫자를 상승


@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: CenterRepository) : ViewModel() {

    var centerResponse = mutableListOf<CenterResponse>()
    var center = mutableListOf<Center>()

    // retro livedata
    private val _retroFinish = MutableLiveData<Int>()
    val retroFinish: LiveData<Int>
        get() = _retroFinish

    // db Livedata
    private val _dbSaveFinish = MutableLiveData<Int>()
    val dbSaveFinish: LiveData<Int>
        get() = _dbSaveFinish

    // ProgressBar Livedata
    private val _progressNumber = MutableLiveData<Int>()
    val progressNumber: LiveData<Int>
        get() = _progressNumber

    //progressBarJob
    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }
    private var progressNum: Int = 0

    init {
        // 1. Retrofit 사용해 비동기 데이터 통신
        viewModelScope.launch {

            for (i in 1..10) {
                // 페이지 1 - 10 부터 비동기 통신

                // ApiResult, handelApi => 수식 간편화를 위해 사용
                // 성공하면 Success 반환 / 에러 생기면 에러별로 반환
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
                    // 통신이 성공적이면 LiveData 통해서 다음 작업 수행
                    _retroFinish.postValue(200)
                }
            }

        }
        // Progress 숫자 제어용 작업
        viewModelScope.launch(job) {
            for (i in 0..80) {
                progressNum = i
                _progressNumber.postValue(i)
                delay(20)
            }
        }
    }
    // 2. 데이터베이스 저장
    fun dataSave() {
        viewModelScope.launch {
            repository.insert(center).let {
                // let 사용해서 작업 끝나면 progressBar 작업 수행
                job.cancel()
                for (i in progressNum..100) {
                    _progressNumber.postValue(i)
                    delay(20)
                }
                // let 사용해서 작업 끝나면 화면 전환
                _dbSaveFinish.postValue(200)
            }
        }
    }

}