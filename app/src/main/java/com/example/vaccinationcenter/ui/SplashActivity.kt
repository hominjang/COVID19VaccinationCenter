package com.example.vaccinationcenter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.vaccinationcenter.MainActivity
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.databinding.ActivitySplashBinding
import com.example.vaccinationcenter.viewmodel.SplashViewModel

import dagger.hilt.android.AndroidEntryPoint
import java.io.File

// 시작화면인 SplashActivity으로 3단계로 나눠집니다.
// 1. Retro 통신이 성공
// 2. DB 저장이 성공
// 3. 화면 이동 (Map fragment)
// 각 단계는 Livedata의 반환으로 진행됩니다.

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        initDataBase()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        splashViewModel.retroFinish.observe(this, Observer {
            if (it == 200) {
                //1. Retro 통신이 성공했으므로, 내부 데이터베이스의 저장시키는 함수를 실행합니다.
                splashViewModel.dataSave()
            }
        })
        splashViewModel.dbSaveFinish.observe(this, Observer {
            if (it == 200) {
                //2. DB 저장이 성공했으므로, Main - MapFragment 로 화면전환합니다.
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        })
        splashViewModel.progressNumber.observe(this, Observer {
            // livedata 통해 제어
            binding.progressText.text = it.toString()
            binding.progress.progress = it.toInt()
        })


    }

    fun initDataBase() {
        // 데이터베이스 데이터 삭제 용도로 구현
        val DB_PATH = "/data/data/" + getPackageName()
        val DB_NAME = "center.db"
        val DB_FULLPATH = "$DB_PATH/databases/$DB_NAME"

        val dbFile = File(DB_FULLPATH)
        if (dbFile.delete()) {
            println("삭제 성공")
        } else {
            println("삭제 실패")
        }
    }
}