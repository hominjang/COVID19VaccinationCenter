package com.example.vaccinationcenter.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View

import com.naver.maps.map.overlay.InfoWindow

import android.view.ViewGroup
import com.example.vaccinationcenter.data.Center
import com.example.vaccinationcenter.databinding.ItemInfoBinding
import com.naver.maps.map.overlay.InfoWindow.DefaultViewAdapter

// 센터 데이터를 받아서 InfoView로 반환하는 어뎁터입니다.
class PointAdapter(context: Context, parent: ViewGroup, center: Center) :
    DefaultViewAdapter(context) {
    private val mContext: Context = context
    private val mParent: ViewGroup = parent
    private val center: Center = center

    override fun getContentView(infoWindow: InfoWindow): View {

        val binding = ItemInfoBinding.inflate(LayoutInflater.from(mContext), mParent, false)
        // 데이터 바인딩이 안되서
        binding.center = center

        // 이런식으로 일일이 바인딩 해줬습니다.
        binding.itemsFacilityName.text = center.facilityName
        binding.itemsCenterName.text = center.centerName
        binding.itemsAddress.text = center.address
        binding.itemsPhoneNumber.text = center.phoneNumber
        binding.itemsUpdatedAt.text = center.updatedAt

        return binding.root
    }

}