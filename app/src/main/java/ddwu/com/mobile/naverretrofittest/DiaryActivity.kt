package ddwu.com.mobile.naverretrofittest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.naverretrofittest.databinding.ActivityDiaryBinding
import ddwu.com.mobile.naverretrofittest.databinding.ActivityMainBinding
import ddwu.com.mobile.naverretrofittest.ui.HospitalAdapter

class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    private lateinit var dbHelper: HospitalDBHelper
    private lateinit var adapter: HospitalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = HospitalDBHelper(this)
        val dataFromDB = dbHelper.getAllPartialData() // 수정된 메소드 이름 사용

        adapter = HospitalAdapter(null)
        binding.diaries.adapter = adapter
        binding.diaries.layoutManager = LinearLayoutManager(this)

//        // Item -> PartialItem으로 변경됨
//        adapter.books = dataFromDB
//        adapter.notifyDataSetChanged()
    }
}