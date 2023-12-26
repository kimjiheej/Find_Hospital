package ddwu.com.mobile.naverretrofittest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import ddwu.com.mobile.naverretrofittest.data.HospitalRoot
import ddwu.com.mobile.naverretrofittest.databinding.ActivityMainBinding
import ddwu.com.mobile.naverretrofittest.network.HospitalAPIService
import ddwu.com.mobile.naverretrofittest.ui.HospitalAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    lateinit var mainBinding: ActivityMainBinding
    lateinit var adapter: HospitalAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var currentLoc: Location? = null // null로 초기화

    private var isFirstLocationUpdate = true

    private lateinit var locCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this)


        adapter = HospitalAdapter(currentLoc)
        mainBinding.rvBooks.adapter = adapter
        mainBinding.rvBooks.layoutManager = LinearLayoutManager(this)

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.hospital_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(HospitalAPIService::class.java)

        mainBinding.btnSearch.setOnClickListener {
            val keyword = mainBinding.etKeyword.text.toString()

            val apiCall = service.getBooksByKeyword(
                resources.getString(R.string.hospital_key),
                "5", "30", keyword
            )

            apiCall.enqueue(object : Callback<HospitalRoot> {
                override fun onResponse(
                    call: Call<HospitalRoot>,
                    response: Response<HospitalRoot>
                ) {
                    if (response.isSuccessful) {
                        val root: HospitalRoot? = response.body()
                        adapter.books = root?.items
                        adapter.notifyDataSetChanged()
                    } else {
                        // Handle failure
                    }
                }

                override fun onFailure(call: Call<HospitalRoot>, t: Throwable) {
                    // Handle failure
                }
            })
        }
        locCallback = object : LocationCallback() {
            @SuppressLint("NewApi")
            override fun onLocationResult(locResult: LocationResult) {
                if (isFirstLocationUpdate) {
                    locResult.locations.firstOrNull()?.let { location ->
                        currentLoc = location // 현재 위치 정보 업데이트
                        adapter.updateLocation(location) // Adapter에 위치 정보 전달

                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            5
                        ) { addresses ->
                            CoroutineScope(Dispatchers.Main).launch {
//                                showData("Latitude: ${location.latitude}, Longitude: ${location.longitude}")
//                                showData(addresses?.get(0)?.getAddressLine(0).toString())
                                isFirstLocationUpdate = false
                                fusedLocationClient.removeLocationUpdates(locCallback)
                            }
                        }
                    }
                }
            }
        }

        mainBinding.diary.setOnClickListener {
            val intent = Intent(this@MainActivity, DiaryActivity::class.java)
            startActivity(intent)
        }
    }


    private val locRequest = LocationRequest.Builder(5000)
        .setMinUpdateIntervalMillis(3000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    @SuppressLint("MissingPermission")
    private fun startLocUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locRequest,
            locCallback,
            null
        )
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locCallback)
    }

    private fun checkPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            showData("Permissions are already granted")
            startLocUpdates()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {

                    startLocUpdates()
                }
                else -> {
                    showData("Location permissions are required")
                }
            }
        }

    private fun showData(data: String) {
        Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
    }
}

