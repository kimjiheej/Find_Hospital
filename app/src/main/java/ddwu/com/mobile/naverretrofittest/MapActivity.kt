package ddwu.com.mobile.naverretrofittest

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var lctn: String? = null // lctn 정보를 저장할 변수 선언

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

        // lctn 정보를 이용하여 주소를 좌표로 변환하는 코드를 작성해야 합니다.
        val location = getLocationFromAddress(lctn) // 주소를 좌표로 변환하는 함수 호출
        location?.let {
            val instNm = intent.getStringExtra("inst_nm") // 가져오려는 instNm 정보
            val telNo = intent.getStringExtra("telno") // 가져오려는 telNo 정보

            // 추가된 마커를 변수에 저장
            val marker = if (instNm != null && telNo != null) {
                addMarker(it, instNm, telNo)
            } else {
                null // 마커가 추가되지 않을 경우 null 반환
            }

            moveCamera(it) // 카메라 이동

            // 마커 클릭 이벤트 처리
            mMap.setOnMarkerClickListener { clickedMarker ->
                if (marker != null && clickedMarker == marker) {
                    clickedMarker.showInfoWindow() // 클릭된 마커의 InfoWindow 표시
                }
                true // true를 반환하여 이벤트 소비했음을 알림
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
            .snippet(telNo) // telNo를 마커의 설명으로 설정

        return mMap.addMarker(markerOptions)
    }

    // 카메라를 이동하는 함수
    private fun moveCamera(location: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}
