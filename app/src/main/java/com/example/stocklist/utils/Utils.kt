package com.example.stocklist.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val DATA = "DATA"

    fun getStringSet(context: Context, string: String, defValue: String): String? {
        val sp: SharedPreferences = context.getSharedPreferences(DATA, 0)
        try {
            return sp.getString(string, defValue)
        } catch (e: Exception) {
            return ""
        }
    }

    fun setStringSet(context: Context, string: String, value: String) {
        val sp: SharedPreferences = context.getSharedPreferences(DATA, 0)
        val edit = sp.edit()
        edit.putString(string, value)
        edit.apply()
    }


    fun getIntSet(context: Context, string: String, defValue: Int): Int {
        val sp: SharedPreferences = context.getSharedPreferences(DATA, 0)
        try {
            return sp.getInt(string, defValue)
        } catch (e: Exception) {
            return 0
        }
    }

    fun setIntSet(context: Context, string: String, value: Int) {
        val sp: SharedPreferences = context.getSharedPreferences(DATA, 0)
        val edit = sp.edit()
        edit.putInt(string, value)
        edit.apply()
    }

    fun getNowhhmmss(): String {
        val sdFormat = SimpleDateFormat("hh:mm:ss")
        val date = Date()
        return sdFormat.format(date)
    }

    fun formatStock(stockPrice: String?): String? {
        var stockPrice = stockPrice ?: return "-"
        /**
         * > 1000 5
         * 500~1000 1
         * 100~500 0.5
         * 50~100 0.1
         * 10~50 0.05
         * < 10 0.01
         */
        return try {
            stockPrice = stockPrice.replace(",", "")
            val num = BigDecimal(stockPrice)
            num.setScale(2, BigDecimal.ROUND_HALF_UP)
            val df = DecimalFormat()

            if (stockPrice.toFloat() < 10) {
                df.applyPattern("##0.00;" + "-##0.00")
            } else if (stockPrice.toFloat() >= 10 && stockPrice.toFloat() < 50) {
                df.applyPattern("##0.00;" + "-##0.00")
            } else if (stockPrice.toFloat() >= 50 && stockPrice.toFloat() < 100) {
                df.applyPattern("##0.00;" + "-##0.00")
            } else if (stockPrice.toFloat() >= 100 && stockPrice.toFloat() < 500) {
                df.applyPattern("##0.0;" + "-##0.0")
            } else if (stockPrice.toFloat() >= 500 && stockPrice.toFloat() < 1000) {
                df.applyPattern("##0;" + "-##0")
            } else {
                df.applyPattern("##0;" + "-##0")
            }

            df.format(num)
        } catch (e: Exception) {
            "-"
        }
    }

    fun formatTick(price: String, tick: String?): String? {
        var price = price ?: return "-"
        var tick = tick ?: return "-"
        /**
         * > 1000 5
         * 500~1000 1
         * 100~500 0.5
         * 50~100 0.1
         * 10~50 0.05
         * < 10 0.01
         */
        return try {
            price = price.replace(",", "")
            var num = BigDecimal(tick).setScale(2, BigDecimal.ROUND_HALF_UP)
            val df = DecimalFormat()

            if (price.toFloat() < 10) {  //0.01
                df.applyPattern("##0.00;" + "-##0.00")
            } else if (price.toFloat() >= 10 && price.toFloat() < 50) { //0.05
                df.applyPattern("##0.05;" + "-##0.05")
            } else if (price.toFloat() >= 50 && price.toFloat() < 100) { //0.1
                num = BigDecimal(tick).setScale(1, BigDecimal.ROUND_HALF_UP)
                df.applyPattern("##0.00;" + "-##0.00")
            } else if (price.toFloat() >= 100 && price.toFloat() < 500) { //0.5
                num = BigDecimal(tick).setScale(1, BigDecimal.ROUND_HALF_UP)
                df.applyPattern("##0.5;" + "-##0.5")
            } else if (price.toFloat() >= 500 && price.toFloat() < 1000) { //1
                df.applyPattern("##0;" + "-##0")
            } else {//5
                df.applyPattern("##0;" + "-##0")
            }

            df.format(num)
        } catch (e: Exception) {
            "-"
        }
    }

    fun formatFloat(strNum: String?): String? {
        var strNum = strNum ?: return "-"
        return try {
            strNum = strNum.replace(",", "")
            val num = BigDecimal(strNum)
            val df = DecimalFormat()
            df.applyPattern("###,###,##0.00;" + "-###,###,##0.00")
            df.format(num)
        } catch (e: Exception) {
            "-"
        }
    }

}