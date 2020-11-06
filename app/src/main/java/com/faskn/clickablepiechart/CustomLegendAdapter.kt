package com.faskn.clickablepiechart

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.faskn.lib.Slice
import com.faskn.lib.legend.LegendAdapter
import com.faskn.lib.legend.LegendItemViewHolder
import kotlinx.android.synthetic.main.custom_item_legend.view.*


class CustomLegendAdapter: LegendAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomLegendItemViewHolder {
        return CustomLegendItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_legend, parent, false)
        )
    }

    // CREATE YOUR OWN ITEM VIEW HOLDER
    class CustomLegendItemViewHolder(view: View) : LegendItemViewHolder(view) {
       override fun bind(slice: Slice) {
           this.boundItem = slice
           itemView.imageViewCircleIndicator.imageTintList =
               ColorStateList.valueOf(ContextCompat.getColor(itemView.context, slice.color))
           itemView.textViewSliceTitle.text = slice.name
       }
   }
}