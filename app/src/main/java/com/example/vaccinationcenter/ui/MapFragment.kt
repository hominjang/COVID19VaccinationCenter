package com.example.vaccinationcenter.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableMap
import androidx.lifecycle.ViewModelProvider
import com.example.vaccinationcenter.MainApplication
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.data.CenterRepository
import com.example.vaccinationcenter.databinding.FragmentMapBinding
import com.example.vaccinationcenter.viewmodel.MapViewModel
import com.example.vaccinationcenter.viewmodel.ViewModelFactory
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    lateinit var mapViewModel: MapViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        factory = ViewModelFactory(CenterRepository(context?.applicationContext as MainApplication))
        mapViewModel = ViewModelProvider(this, factory).get(MapViewModel::class.java)

        binding.viewModel = mapViewModel
        binding.mapView.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.centerList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            for (i in it) {
                val marker = Marker()
                marker.position = LatLng(
                    i.lat.toDouble(),
                    i.lng.toDouble()
                )
                marker.map = naverMap
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        mapViewModel.viewModelStart()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }


}