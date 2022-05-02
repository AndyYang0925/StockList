package com.example.stocklist.utils

import android.content.Context
import android.util.Log
import com.example.stocklist.Constants
import com.example.stocklist.Constants.ANIMATOR_DURATION
import com.example.stocklist.model.Stock
import java.util.*

object PriceManager {

    var timerTask: TimerTask? = null
    var timer: Timer? = null

    fun startTimer(
        context: Context,
        priceRunnable: Runnable,
    ) {

        timerTask = object : TimerTask() {
            override fun run() {
                priceRunnable.run()
            }
        }

        timer = Timer()
        timer!!.schedule(
            timerTask,
            0,
            Utils.getIntSet(context, Constants.KEY_MS, 200).toLong() + ANIMATOR_DURATION
        )
    }


    fun endTimer() {
        if (timer != null)
            timer!!.cancel()
        if (timerTask != null)
            timerTask!!.cancel()
    }

    fun randomUpdateStock(max: Int): ArrayList<Int> {
        val items = ArrayList<Int>()
        val times = (Math.random() * max).toInt() //此次更新幾個

        for (i in 0 until times) {
            val index = (Math.random() * max).toInt()
            if (!items.contains(index))
                items.add(index)
        }

        return items
    }

    fun stockPriceUpdate(stock: Stock): Stock {

        val positiveOrNegative = if (((1..10).random()) % 2 == 0) 1 else -1

        stock.upDown =
            Utils.formatTick(
                stock.closePrice!!,
                (stock.closePrice!!.toFloat() * (Math.random() * 10) * positiveOrNegative / 100).toString()
            )
        stock.upDownRatio =
            (stock.upDown!!.toFloat() / stock.closePrice!!.toFloat() * 100).toString()
        stock.nowPrice = (stock.closePrice!!.toFloat() + stock.upDown!!.toFloat()).toString()

        stock.updateTime = Utils.getNowhhmmss()

        return stock
    }


}