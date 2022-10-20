package com.example.vaccinationcenter.data

import android.R
import android.content.Context
import android.util.Log

import android.widget.TextView

import android.view.LayoutInflater
import android.view.View

import com.naver.maps.map.overlay.InfoWindow

import androidx.annotation.NonNull

import android.view.ViewGroup
import com.example.vaccinationcenter.databinding.ItemInfoBinding
import com.naver.maps.map.overlay.InfoWindow.DefaultViewAdapter


class pointAdapter(context: Context, parent: ViewGroup, center: Center) :
    DefaultViewAdapter(context) {
    private val mContext: Context = context
    private val mParent: ViewGroup = parent
    private val center: Center = center

    override fun getContentView(infoWindow: InfoWindow): View {

        val binding = ItemInfoBinding.inflate(LayoutInflater.from(mContext), mParent, false)

        binding.center = center

        binding.itemsFacilityName.text = center.facilityName
        binding.itemsCenterName.text = center.centerName
        binding.itemsAddress.text = center.address
        binding.itemsPhoneNumber.text = center.phoneNumber
        binding.itemsUpdatedAt.text = center.updatedAt

        return binding.root
    }

}