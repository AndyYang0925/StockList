package com.example.stocklist.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Stock : Serializable {

    @SerializedName("Code")
    var stockId: String? = null

    @SerializedName("Name")
    var stockName: String? = null

    @SerializedName("ClosingPrice")
    var closePrice: String? = null

    var nowPrice: String? = null

    var upDown: String? = null

    var upDownRatio: String? = null

    var updateTime: String? = null

    var isShow = false

    override fun equals(obj: Any?): Boolean { //比較Id
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if ( javaClass != obj.javaClass ) {
            return false
        }
        if (obj is Stock)
            if (stockId != obj.stockId)
                return false
        return true
    }


}