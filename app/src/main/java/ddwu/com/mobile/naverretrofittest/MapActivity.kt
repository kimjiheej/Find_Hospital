package ddwu.com.mobile.naverretrofittest

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var lctn: String? = null // lctn 정보를 저장할 변수 선언

    var centerMarker : Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        // intent로부터 lctn 정보 받아오기
        lctn = intent.getStringExtra("lctn")


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // 현재 MapActivity 종료
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

       val currentLoc = intent.getParcelableExtra<Location>("current_loc")


        currentLoc?.let { location ->
            val currentLatLng = LatLng(currentLoc.latitude, currentLoc.longitude)
            animateCamera(currentLatLng)
            addMarker(currentLatLng, "하하", "호호")
        }

        val lctn = intent.getStringExtra("lctn")
        val hospitalLocation = getLocationFromAddress(lctn)


        hospitalLocation?.let {
            val instNm = intent.getStringExtra("inst_nm") // 가져오려는 instNm 정보
            val telNo = intent.getStringExtra("telno") // 가져오려는 telNo 정보

            val marker = if (instNm != null && telNo != null) {
                addMarker(it, instNm, telNo)
            } else {
                null // 마커가 추가되지 않을 경우 null 반환
            }

            animateCamera(it) // 카메라 이동

            // 마커 클릭 이벤트 처리
            mMap.setOnMarkerClickListener { clickedMarker ->
                if (marker != null && clickedMarker == marker) {
                    clickedMarker.showInfoWindow() // 클릭된 마커의 InfoWindow 표시
                }
                true // true를 반환하여 이벤트 소비했음을 알림
            }

            currentLoc?.let { currentLocation ->
                drawLine(
                    LatLng(currentLoc.latitude,currentLoc.longitude),
                    LatLng(it.latitude, it.longitude)
                )
            }
        }
    }


    // 주소를 좌표로 변환하는 함수
    private fun getLocationFromAddress(strAddress: String?): LatLng? {
        val geocoder = Geocoder(this)
        val addressList: List<Address>?

        try {
            // Geocoder를 이용하여 주소를 좌표로 변환
            addressList = lctn?.let { geocoder.getFromLocationName(it, 1) }
            if (addressList != null && addressList.isNotEmpty()) {
                val location = addressList[0]
                return LatLng(location.latitude, location.longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // 마커를 추가하는 함수
    private fun addMarker(location: LatLng, instNm: String, telNo: String): Marker? {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(instNm) // instNm을 마커의 제목으로 설정
            .snippet("전화번호: " + telNo) // telNo를 마커의 설명으로 설정

        return mMap.addMarker(markerOptions)
    }

    // 카메라를 이동하는 함수
    private fun moveCamera(location: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun animateCamera(location: LatLng){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,17f))
    }
    fun drawLine(startPoint: LatLng, endPoint: LatLng) {
        val polylineOptions = PolylineOptions()
            .color(Color.RED)
            .width(5f)
            .add(startPoint, endPoint)

        mMap.addPolyline(polylineOptions)
    }
}
