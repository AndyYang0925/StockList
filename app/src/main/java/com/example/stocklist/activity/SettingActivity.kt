package com.example.stocklist.activity

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stocklist.Constants.KEY_MS
import com.example.stocklist.R
import com.example.stocklist.adapter.CheckBoxAdapter
import com.example.stocklist.model.Stock
import com.example.stocklist.utils.Utils
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    lateinit var checkBoxAdapter: CheckBoxAdapter
    var stockList = ArrayList<Stock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val bundle = this.intent.extras
        stockList.addAll(bundle!!.getSerializable("stockList") as ArrayList<Stock>)

        initView()

        initListener()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setMS()
    }

    fun initView() {


        checkBoxAdapter = CheckBoxAdapter(this)
        rv_check_box.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_check_box.adapter = checkBoxAdapter
        checkBoxAdapter.bindDataSource(stockList)


        et_ms.setText(Utils.getIntSet(this, KEY_MS, 200).toString())

    }

    fun initListener() {

        ib_back.setOnClickListener {
            setMS()
            finish()
        }

        cb_check_all.setOnCheckedChangeListener { view, isCheck ->

            if (isCheck) {
                for (i in stockList.indices)
                    stockList[i].isShow = true

                if (!rv_check_box.isComputingLayout && rv_check_box.scrollState == SCROLL_STATE_IDLE)
                    checkBoxAdapter.bindDataSource(stockList)

                intent.apply {
                    putExtra("check_list", stockList)
                }
                setResult(RESULT_OK, intent)
            }

        }

        checkBoxAdapter.setItemClickListener(object : CheckBoxAdapter.OnCheckListener {
            override fun onCheckListener(position: Int) {

                stockList[position].isShow = !stockList[position].isShow

                if (!stockList[position].isShow)
                    cb_check_all.isChecked = false

                intent.apply {
                    putExtra("check_list", stockList)
                }
                setResult(RESULT_OK, intent)

            }
        })

    }

    //    按鍵返回、左上角返回
    fun setMS() {
        try {
            Utils.setIntSet(this, KEY_MS, et_ms.text.toString().toInt())
        } catch (e: Exception) {
            Log.e("set ms error", e.toString())
        }
    }

}