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

class LegendAdapter : RecyclerView.Adapter<LegendAdapter.ItemViewHolder>() {

    private val items = mutableListOf<Slice>()

    var onItemClickListener: ((Slice?) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LegendAdapter.ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_legend,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LegendAdapter.ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setup(items: List<Slice>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var boundItem: Slice? = null

        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(boundItem)
            }
        }

        fun bind(slice: Slice) {
            this.boundItem = slice
            itemView.imageViewCircleIndicator.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(itemView.context, slice.color))
            itemView.textViewSliceTitle.text = slice.name
        }
    }
}