package ddwu.com.mobile.naverretrofittest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import ddwu.com.mobile.naverretrofittest.data.HospitalRoot
import ddwu.com.mobile.naverretrofittest.databinding.ActivityMainBinding
import ddwu.com.mobile.naverretrofittest.network.IBookAPIService
import ddwu.com.mobile.naverretrofittest.ui.BookAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    lateinit var mainBinding : ActivityMainBinding
    lateinit var adapter : BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        adapter = BookAdapter()
        mainBinding.rvBooks.adapter = adapter
        mainBinding.rvBooks.layoutManager = LinearLayoutManager(this)



        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.hospital_url))
            .addConverterFactory( GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IBookAPIService::class.java)

        mainBinding.btnSearch.setOnClickListener {
            val keyword = mainBinding.etKeyword.text.toString()

            val apiCall = service.getBooksByKeyword(
                resources.getString(R.string.hospital_key),
                "5","10",keyword
            )

            apiCall.enqueue(object : Callback<HospitalRoot> {
                override fun onResponse(call: Call<HospitalRoot>, response: Response<HospitalRoot>) {
                    if (response.isSuccessful) {
                        val root: HospitalRoot? = response.body()
                        adapter.books = root?.items
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d(TAG, response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<HospitalRoot>, t: Throwable) {
                    // 실패 시 처리할 내용
                }
            })
        }
    }
}

