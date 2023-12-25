package ddwu.com.mobile.naverretrofittest.ui

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.naverretrofittest.HospitalDBHelper
import ddwu.com.mobile.naverretrofittest.MapActivity
import ddwu.com.mobile.naverretrofittest.data.Item
import ddwu.com.mobile.naverretrofittest.databinding.ListItemBinding
import java.io.IOException


class HospitalAdapter(private var currentLoc: Location?) : RecyclerView.Adapter<HospitalAdapter.BookHolder>() {
    var books: List<Item>? = null

    override fun getItemCount(): Int {
        return books?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val currentItem = books?.get(position)

        currentItem?.let { item ->
            holder.itemBinding.name.text = item.inst_nm // inst_nm을 name TextView에 설정
            holder.itemBinding.type.text = item.type // type을 type TextView에 설정

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, MapActivity::class.java)
                val lctn = item.lctn // lctn 정보 가져오기
                val instNm = item.inst_nm // inst_nm 정보 가져오기
                val telNo = item.telno // telno 정보 가져오기

                // lctn, inst_nm, telno 정보를 MapActivity로 전달
                intent.putExtra("lctn", lctn)
                intent.putExtra("inst_nm", instNm)
                intent.putExtra("telno", telNo)

                currentLoc?.let { location ->
                    // Location 객체 전달
                    intent.putExtra("current_loc", location)

                    // currentLoc(Location 객체)를 주소로 변환하여 String으로 전달
                    try {
                        val geocoder = Geocoder(context)
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (addresses?.isNotEmpty()!!) {
                            val address = addresses[0].getAddressLine(0)
                            intent.putExtra("current_address", address)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener {
                val context = holder.itemView.context
                val builder = AlertDialog.Builder(context)
                builder.setTitle("저장하시겠습니까?")

                builder.setPositiveButton("예") { dialog, which ->
                    // 예를 클릭했을 때 수행할 작업
                    val dbHelper = HospitalDBHelper(context)
                    val currentItem = books?.get(position)

                    currentItem?.let { item ->
                        dbHelper.insertData(item.inst_nm, item.type)
                    }
                    Toast.makeText(context, "아이템을 저장했습니다.", Toast.LENGTH_SHORT).show()
                }

                builder.setNegativeButton("아니요") { dialog, which ->
                    // 아니요를 클릭했을 때 수행할 작업
                    Toast.makeText(context, "저장을 취소했습니다.", Toast.LENGTH_SHORT).show()
                }

                val dialog = builder.create()
                dialog.show()

                true // 이벤트 소비
            }
            holder.itemBinding.type.setOnClickListener {
                clickListener?.onItemClick(it, position)
            }
        }
    }

    class BookHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    fun updateLocation(newLocation: Location) {
        currentLoc = newLocation
        notifyDataSetChanged() // 위치 정보가 업데이트되면 RecyclerView를 새로 고침
    }


}