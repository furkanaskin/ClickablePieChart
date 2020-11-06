package com.faskn.lib.legend

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.faskn.lib.R
import com.faskn.lib.Slice
import kotlinx.android.synthetic.main.item_legend.view.*

open class LegendAdapter : RecyclerView.Adapter<LegendItemViewHolder>() {

    protected val items = mutableListOf<Slice>()
    var onItemClickListener: ((Slice?) -> Unit)? = null

    fun setup(items: List<Slice>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendItemViewHolder {
        return LegendItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_legend, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LegendItemViewHolder, position: Int) {
        holder.bind(items[position])

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(items[position])
        }
    }
}


open class LegendItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var boundItem: Slice? = null
    open fun bind(slice: Slice) {
        boundItem = slice
        itemView.imageViewCircleIndicator.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, slice.color))
        itemView.textViewSliceTitle.text = slice.name
    }
}





