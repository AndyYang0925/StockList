package com.example.stocklist.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stocklist.Constants.LIST50
import com.example.stocklist.R
import com.example.stocklist.adapter.CellStockAdapter
import com.example.stocklist.adapter.RowHeaderAdapter
import com.example.stocklist.api.ApiService
import com.example.stocklist.api.AppClientManager
import com.example.stocklist.model.Stock
import com.example.stocklist.utils.PriceManager
import com.example.stocklist.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    val TAG = javaClass.simpleName

    var apiService: ApiService? = null

    var allStockList = ArrayList<Stock>()
    var stockList = ArrayList<Stock>()

    var rowHeaderAdapter: RowHeaderAdapter? = null
    var cellStockAdapter: CellStockAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = AppClientManager.client.create(ApiService::class.java)

        initView()

        initListener()

        apiGetStockPrice()

    }

    override fun onResume() {
        super.onResume()
        PriceManager.startTimer(this@MainActivity) { updatePrice() }
    }

    override fun onPause() {
        super.onPause()
        PriceManager.endTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        PriceManager.endTimer()
    }


    fun initView() {

        tv_date.text =
            (Calendar.getInstance().get(Calendar.YEAR) - 1911).toString() + "年" +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1).toString() + "月" +
                    (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).toString() + "日" +
                    "\n當日成交資訊(股)"

        rowHeaderAdapter = RowHeaderAdapter(this)
        rv_row_header.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_row_header.adapter = rowHeaderAdapter


        cellStockAdapter = CellStockAdapter(this)
        rv_stock.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_stock.adapter = cellStockAdapter


    }

    fun initListener() {

        ib_refresh.setOnClickListener { apiGetStockPrice() }

        ib_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("stockList", allStockList)
            intent.putExtras(bundle)
            resultSettingLauncher.launch(intent)
        }

        hsv_cell.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            hsv_column_header.scrollTo(scrollX, scrollY)
        })

        hsv_column_header.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            hsv_cell.scrollTo(scrollX, scrollY)
        })

        rv_stock.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (rv_stock.scrollState != RecyclerView.SCROLL_STATE_IDLE)
                    rv_row_header.scrollBy(dx, dy)
            }
        })

        rv_row_header.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (rv_row_header.scrollState != RecyclerView.SCROLL_STATE_IDLE)
                    rv_stock.scrollBy(dx, dy)
            }
        })

    }

    // onActivityResult
    private var resultSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                if (result.data != null && result.data!!.extras != null || result.data!!.extras!!.get(
                        "check_list"
                    ) == null
                ) {
                    stockList.clear()
                    allStockList.clear()
                    allStockList.addAll(result.data!!.extras!!.get("check_list") as Collection<Stock>)

                    for (i in allStockList.indices) {
                        if (allStockList[i].isShow) {
                            stockList.add((allStockList[i]))
                        }
                    }

                    rowHeaderAdapter?.bindDataSource(stockList)
                    cellStockAdapter?.bindDataSource(stockList)

                }
            }
        }

    fun updatePrice() {
        if (cellStockAdapter == null || stockList.size == 0) //預防股票列表還沒建置
            return

        runOnUiThread {
            val items = ArrayList<Int>()
            items.addAll(PriceManager.randomUpdateStock(stockList.size)) //亂數取得要更新哪些股票
            items.sort()

            for (i in items.indices)
                PriceManager.stockPriceUpdate(stockList[items[i]]) //亂數更新股票漲跌

            cellStockAdapter?.updateRow(items)
            rowHeaderAdapter?.updateRow(items)


        }

    }

    // api抓昨收
    fun apiGetStockPrice() {

        apiService!!.getStockPriceList()
            .enqueue(object : retrofit2.Callback<List<Stock>> {
                override fun onResponse(
                    call: Call<List<Stock>>,
                    response: Response<List<Stock>>
                ) {
                    val p = response.body()
                    if (p != null) {
                        stockList.clear()
                        allStockList.clear()
                        val map: MutableMap<String, Stock> = mutableMapOf()
                        for (i in p.indices) {
                            if (p[i].stockId != null)
                                map[p[i].stockId!!] = p[i]
                        }

                        for (i in LIST50.indices) { //加入市值50大股票
                            val stock = if (map[LIST50[i]] == null) Stock() else map[LIST50[i]]
                            stock!!.isShow = true
                            stock.nowPrice = stock.closePrice
                            stock.upDown = "0.00"
                            stock.upDownRatio = "0.00"
                            stock.updateTime = Utils.getNowhhmmss()
                            stock.isShow = true
                            stockList.add(stock)
                            allStockList.add(stock)
                        }

                        rowHeaderAdapter?.bindDataSource(stockList)
                        cellStockAdapter?.bindDataSource(stockList)

                    }
                }

                override fun onFailure(call: Call<List<Stock>>, t: Throwable) {
                    Log.e(TAG, "error: $t")
                }
            })

    }

}