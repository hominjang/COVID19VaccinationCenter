package com.example.vaccinationcenter.ui

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.vaccinationcenter.R
import com.example.vaccinationcenter.adapter.PointAdapter
import com.example.vaccinationcenter.databinding.FragmentMapBinding
import com.example.vaccinationcenter.viewmodel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint

// 지도화면인 MapFragment는 3가지 기능으로 나눠집니다.
// 1. 지도 (naverMap)
// 1-1) 마커 (DB에서 정보 토대로 marker + listener 추가)
// 1-2) 정보창 (마커 클릭해서 listener 호출되면, PointerAdapter 사용해서 view 반환)

// 2. 퍼미션 (TedPermission 통해서 구현)

// 3. 현재 위치 (FusedLocation 통해서 구현)
// 3-1) 퍼미션 되있으면, 자기 위치 호출
// 3-2) 퍼미션 안되있으면, 퍼미션 함수 호츨


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    val mapViewModel: MapViewModel by viewModels()

    private lateinit var naverMap: NaverMap

    // 현재 위치 호출용 변수
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // 지도 정보창 변수
    var infoWindow: InfoWindow = InfoWindow()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        // 버튼 클릭용 데이터 바인딩
        binding.viewModel = mapViewModel
        binding.mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.centerList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            // 지도 구성이 끝나서 마커 찍을 수 있음.
            // 1-1) 마커 (DB에서 정보 토대로 marker + listener 추가)
            // 마커에 좌표/색깔/클릭리스너 추가

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
                    // 클릭 될 때
                    // 정보창 없으면, 정보창 열기
                    if (marker.infoWindow == null) {
                        // custom view를 위해 adapter를 통해 구현
                        infoWindow.adapter =
                            PointAdapter(requireActivity(), binding.root as ViewGroup, i)
                        infoWindow.open(marker)
                    } else {
                        // 있으면, 열려있는 정보창 닫기
                        infoWindow.close()
                    }

                    // 클릭한 해당 마커로 지도 카메라 이동
                    val cameraUpdate =
                        CameraUpdate.scrollTo(LatLng(i.lat.toDouble(), i.lng.toDouble()))
                    naverMap.moveCamera(cameraUpdate)
                    false
                }
                // 설정된 마커 찍기
                marker.map = naverMap
            }
        })

        mapViewModel.btnClick.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            // 클릭 했을 때, 퍼미션 했는지 확인
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 퍼미션 안되어있으면, 퍼미션 허가문 호출
                requestPermissions()
            } else {
                // 되어있으면, 현재위치 호출
                var currentLocation: Location?
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        currentLocation = location

                        naverMap.locationOverlay.run {
                            isVisible = true
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

                    }
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 지도 설정 끝나면, DB 가져와서 마커 찍을 준비
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
                    mapViewModel.findMyLocation()
                }

                //권한이 거부됐을 때
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(requireContext(), "권한이 거부되어 위치를 불러올 수 없습니다", Toast.LENGTH_SHORT)
                        .show()
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
