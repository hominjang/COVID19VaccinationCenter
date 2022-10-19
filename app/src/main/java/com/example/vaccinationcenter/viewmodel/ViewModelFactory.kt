package com.example.vaccinationcenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vaccinationcenter.MainApplication
import com.example.vaccinationcenter.data.CenterRepository

class ViewModelFactory(private val repository: CenterRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CenterRepository::class.java).newInstance(repository)
    }
}