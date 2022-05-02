package com.example.stocklist.adapter

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stocklist.Constants.ANIMATOR_DURATION
import com.example.stocklist.R
import com.example.stocklist.model.Stock
import com.example.stocklist.utils.Utils

class CellStockAdapter constructor(
    val mContext: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var stockList = ArrayList<Stock>()
    var animatorList = ArrayList<Int>()

    var animatorPrice: ValueAnimator? = null
    
    class ChildItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rlBg: RelativeLayout = itemView.findViewById(R.id.rl_bg)
        val tvData1: TextView = itemView.findViewById(R.id.tv_cell_data_1)
        val tvData2: TextView = itemView.findViewById(R.id.tv_cell_data_2)
        val tvData3: TextView = itemView.findViewById(R.id.tv_cell_data_3)
        val tvData4: TextView = itemView.findViewById(R.id.tv_cell_data_4)
        val tvData5: TextView = itemView.findViewById(R.id.tv_cell_data_5)
        val line: View = itemView.findViewById(R.id.line)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_cell_item, parent, false)
             return ChildItemHolder(view)
    }

    override fun getItemCount(): Int {
        return if (stockList.size != 0) stockList.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ChildItemHolder) {

            val stock = stockList[holder.layoutPosition]

            holder.tvData1.text = Utils.formatStock(stock.closePrice)
            holder.tvData2.text = Utils.formatStock(stock.nowPrice)
            holder.tvData3.text = Utils.formatFloat(stock.upDown)
            holder.tvData4.text = Utils.formatFloat(stock.upDownRatio) + "%"
            holder.tvData5.text = stock.updateTime

            var color = when {
                stock.upDown?.toFloat() ?: 0f > 0f -> ContextCompat.getColor(
                    mContext,
                    R.color.stockRedColor
                )
                stock.upDown?.toFloat() ?: 0f < 0f -> ContextCompat.getColor(
                    mContext,
                    R.color.stockGreenColor
                )
                else -> ContextCompat.getColor(mContext, R.color.stockWhiteColor)
            }

            holder.tvData1.setTextColor(color)
            holder.tvData2.setTextColor(color)
            holder.tvData3.setTextColor(color)
            holder.tvData4.setTextColor(color)

            animatorPrice = ObjectAnimator.ofInt(
                holder.line,
                "backgroundColor",
                color,
                color,
                ContextCompat.getColor(mContext, R.color.bgColor)
            )
            animatorPrice!!.repeatMode = ValueAnimator.RESTART
            animatorPrice!!.repeatCount = 0
            animatorPrice!!.duration = ANIMATOR_DURATION.toLong()
            animatorPrice!!.setEvaluator(ArgbEvaluator())
            if (animatorList.contains(holder.layoutPosition))
                animatorPrice!!.start()

        }
    }

    fun bindDataSource(list: ArrayList<Stock>) {
        val size = stockList.size
        stockList.clear();
        notifyItemRangeRemoved(0, size);
        stockList.addAll(list);
        notifyItemRangeInserted(0, itemCount)
    }

    fun updateRow(items: ArrayList<Int>) {

        if (stockList.isNullOrEmpty())
            return

        animatorList.clear()
        animatorList.addAll(items)

        for (i in items.indices)
            notifyItemChanged(items[i])

    }

}