package ddwu.com.mobile.naverretrofittest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.naverretrofittest.databinding.ActivityDiaryBinding
import ddwu.com.mobile.naverretrofittest.ui.HospitalAdapter


class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    private lateinit var dbHelper: HospitalDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = HospitalDBHelper(this)

        val btnRemove = findViewById<Button>(R.id.btnRemove)
        btnRemove.setOnClickListener {
            val intent = Intent(this, RemoveActivity::class.java)
            startActivity(intent)
        }
        printPartialData()
}

    private fun printPartialData() {
        val partialItemList = dbHelper.getAllPartialData()

        val hospitalListTextView: TextView = findViewById(R.id.HospitalList)

        val stringBuilder = StringBuilder()
        for (partialItem in partialItemList) {
            stringBuilder.append(" Name: ${partialItem.name}, Type: ${partialItem.type}\n")
        }

        hospitalListTextView.text = stringBuilder.toString()
    }
}