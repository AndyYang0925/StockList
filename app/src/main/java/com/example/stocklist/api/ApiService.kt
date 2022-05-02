package com.example.stocklist.api

import com.example.stocklist.Constants.STOCK_PRICE_LINK
import com.example.stocklist.model.Stock
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET(STOCK_PRICE_LINK)
    fun getStockPriceList(
    ): Call<List<Stock>>

}