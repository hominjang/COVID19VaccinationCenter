package com.example.vaccinationcenter.ui

import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableMap
import androidx.lifecycle.ViewModelProvider
import com.example.vaccinationcenter.MainActivity
import com.example.vaccinationcenter.MainApplication
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.data.CenterRepository
import com.example.vaccinationcenter.data.pointAdapter
import com.example.vaccinationcenter.databinding.FragmentMapBinding
import com.example.vaccinationcenter.viewmodel.MapViewModel
import com.example.vaccinationcenter.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.internal.ViewUtils.getContentView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import java.util.jar.Manifest


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    lateinit var mapViewModel: MapViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var naverMap: NaverMap

    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var infoWindow: InfoWindow = InfoWindow()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        factory = ViewModelFactory(CenterRepository(context?.applicationContext as MainApplication))
        mapViewModel = ViewModelProvider(this, factory).get(MapViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.viewModel = mapViewModel
        binding.mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestPermissions()

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

                if (i.centerType != "지역") {
                    marker.icon = MarkerIcons.BLACK
                }


                marker.onClickListener = Overlay.OnClickListener() {

                    if (marker.infoWindow == null) {
                        infoWindow.adapter =
                            pointAdapter(requireActivity(), binding.root as ViewGroup, i)
                        infoWindow.open(marker)
                    } else {
                        infoWindow.close()
                    }

                    val cameraUpdate =
                        CameraUpdate.scrollTo(LatLng(i.lat.toDouble(), i.lng.toDouble()))
                    naverMap.moveCamera(cameraUpdate)
                    false
                }

                marker.map = naverMap
            }
        })

        mapViewModel.btnClick.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                var currentLocation: Location?
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        currentLocation = location
                        // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다. 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
                        // 파랑색 점, 현재 위치 표시
                        naverMap.locationOverlay.run {
                            isVisible = false
                            position =
                                LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                        }

                        // 카메라 현재위치로 이동
                        val cameraUpdate = CameraUpdate.scrollTo(
                            LatLng(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude
                            )
                        )
                        naverMap.moveCamera(cameraUpdate)

                        // 빨간색 마커 현재위치로 변경
                    }
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        mapViewModel.viewModelStart()
    }

    private fun requestPermissions() {
        // 내장 위치 추적 기능 사용
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                //권한이 허용됐을 때
                override fun onPermissionGranted() {
                    mapViewModel.permissionGranted()
                }

                //권한이 거부됐을 때
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(requireContext(), "카메라 불가!", Toast.LENGTH_SHORT).show()
                }
            })
            .setPermissions(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ).check()


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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
