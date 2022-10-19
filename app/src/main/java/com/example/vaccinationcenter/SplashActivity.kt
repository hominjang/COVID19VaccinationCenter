package com.example.vaccinationcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vaccinationcenter.data.CenterRepository
import com.example.vaccinationcenter.databinding.ActivitySplashBinding
import com.example.vaccinationcenter.databinding.FragmentMapBinding
import com.example.vaccinationcenter.viewmodel.MapViewModel
import com.example.vaccinationcenter.viewmodel.SplashViewModel
import com.example.vaccinationcenter.viewmodel.ViewModelFactory
import java.io.File

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    lateinit var splashViewModel: SplashViewModel
    private lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBase()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        factory = ViewModelFactory(CenterRepository(applicationContext as MainApplication))
        splashViewModel = ViewModelProvider(this, factory)[SplashViewModel::class.java]

        splashViewModel.retroFinish.observe(this, Observer {
            if (it == 200) {
                splashViewModel.dataSave()
            }
        })
        splashViewModel.dbSaveFinish.observe(this, Observer {
            if (it == 200) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        })
        splashViewModel.progressNumber.observe(this, Observer {
            binding.progressText.text = it.toString()
            binding.progress.progress = it.toInt()
        })


    }
    fun initDataBase(){
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