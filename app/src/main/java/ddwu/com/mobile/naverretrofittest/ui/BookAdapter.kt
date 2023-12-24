package ddwu.com.mobile.naverretrofittest.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.naverretrofittest.MapActivity
import ddwu.com.mobile.naverretrofittest.data.Item
import ddwu.com.mobile.naverretrofittest.databinding.ListItemBinding


class BookAdapter : RecyclerView.Adapter<BookAdapter.BookHolder>() {
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

                context.startActivity(intent)
            }

            holder.itemBinding.type.setOnClickListener {
                clickListener?.onItemClick(it, position)
            }
        }
    }




    class BookHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListner {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListner? = null

    fun setOnItemClickListener(listener: OnItemClickListner) {
        this.clickListener = listener
    }


}