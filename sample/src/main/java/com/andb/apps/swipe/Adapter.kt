package com.andb.apps.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

const val VIEW_TYPE_1 = 487334
const val VIEW_TYPE_2 = 843934

class Adapter(private val list: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH1(view: View) : RecyclerView.ViewHolder(view)
    class VH2(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return when(viewType){
            VIEW_TYPE_1 -> VH1(view)
            else -> VH2(view)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_1 else VIEW_TYPE_2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            listText.text = list[position]
        }
    }

}
