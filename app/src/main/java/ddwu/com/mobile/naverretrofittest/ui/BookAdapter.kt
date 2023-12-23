package ddwu.com.mobile.naverretrofittest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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