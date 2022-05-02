package com.example.stocklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stocklist.R
import com.example.stocklist.model.Stock

class CheckBoxAdapter constructor(
    val mContext: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var stockList = ArrayList<Stock>()

    var mListener: OnCheckListener? = null

    class ChildItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbStock = itemView.findViewById<CheckBox>(R.id.cb_stock)
        val tvStockId = itemView.findViewById<TextView>(R.id.tv_stock_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_check_box_item, parent, false)
        return ChildItemHolder(view)
    }

    override fun getItemCount(): Int {
        return if (stockList.size != 0) stockList.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ChildItemHolder) {

            val stock = stockList[holder.layoutPosition]

            holder.tvStockId.text = stock.stockId

            holder.cbStock.isChecked =stock.isShow

            holder.cbStock.setOnClickListener {
                mListener!!.onCheckListener(position)
            }

        }
    }

    fun bindDataSource(list: ArrayList<Stock>) {
        val size = stockList.size
        stockList.clear();
        notifyItemRangeRemoved(0, size);
        stockList.addAll(list);
        notifyItemRangeInserted(0, itemCount)
    }

    fun setItemClickListener(ic: OnCheckListener) {
        mListener = ic
    }

    interface OnCheckListener {
        fun onCheckListener(position: Int)
    }

}