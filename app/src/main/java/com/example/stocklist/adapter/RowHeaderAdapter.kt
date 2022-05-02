package com.example.stocklist.adapter

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
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

class RowHeaderAdapter constructor(
    val mContext: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var stockList = ArrayList<Stock>()
    var animatorList = ArrayList<Int>()

    var animatorPrice: ValueAnimator? = null

    class ChildItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rlBg: RelativeLayout = itemView.findViewById(R.id.rl_bg)
        val tvRowHeader: TextView = itemView.findViewById(R.id.tv_row_header_data)
        val line: View = itemView.findViewById(R.id.line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_row_header_item, parent, false)
        return ChildItemHolder(view)
    }

    override fun getItemCount(): Int {
        return if (stockList.size != 0) stockList.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ChildItemHolder) {

            val stock = stockList[holder.layoutPosition]

            holder.tvRowHeader.text  = stock.stockName

            animatorPrice = ObjectAnimator.ofInt(
                holder.line,
                "backgroundColor",
                when {
                    stock.upDown?.toFloat() ?: 0f > 0f -> ContextCompat.getColor(
                        mContext,
                        R.color.stockRedColor
                    )
                    stock.upDown?.toFloat() ?: 0f < 0f -> ContextCompat.getColor(
                        mContext,
                        R.color.stockGreenColor
                    )
                    else -> ContextCompat.getColor(mContext, R.color.stockWhiteColor)
                },
                when {
                    stock.upDown?.toFloat() ?: 0f > 0f -> ContextCompat.getColor(
                        mContext,
                        R.color.stockRedColor
                    )
                    stock.upDown?.toFloat() ?: 0f < 0f -> ContextCompat.getColor(
                        mContext,
                        R.color.stockGreenColor
                    )
                    else -> ContextCompat.getColor(mContext, R.color.stockWhiteColor)
                },
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
