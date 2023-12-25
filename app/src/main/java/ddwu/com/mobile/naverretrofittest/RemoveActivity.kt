package ddwu.com.mobile.naverretrofittest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ddwu.com.mobile.naverretrofittest.databinding.ActivityRemoveBinding
import ddwu.com.mobile.naverretrofittest.ui.HospitalAdapter

class RemoveActivity : AppCompatActivity() {

    private lateinit var removeBinding: ActivityRemoveBinding
    private lateinit var dbHelper: HospitalDBHelper
    private lateinit var adapter: HospitalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        removeBinding = ActivityRemoveBinding.inflate(layoutInflater)
        setContentView(removeBinding.root)

        dbHelper = HospitalDBHelper(this)
        adapter = HospitalAdapter(null) // 사용 중인 어댑터에 맞게 수정하세요

        val btnRemoveHospital = findViewById<Button>(R.id.btnRemoveHospital)
        val etRemoveFood = findViewById<EditText>(R.id.etRemoveFood)
        val btnReturn = findViewById<Button>(R.id.btnReturn)

        btnRemoveHospital.setOnClickListener {
            val hospitalName = etRemoveFood.text.toString()
            if (hospitalName.isNotEmpty()) {
                dbHelper.deleteData(hospitalName)
                adapter.notifyDataSetChanged() // 데이터 변경 알림
                Toast.makeText(this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        btnReturn.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
            finish() // 현재 RemoveActivity를 종료하고 DiaryActivity로 돌아갑니다.
        }
    }
}
