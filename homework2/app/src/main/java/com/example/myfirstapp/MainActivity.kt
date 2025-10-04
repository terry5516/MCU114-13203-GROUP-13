package com.example.myfirstapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // 宣告變數
    private lateinit var txtShow: TextView
    private lateinit var btnZero: Button
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnFour: Button
    private lateinit var btnFive: Button
    private lateinit var btnSix: Button
    private lateinit var btnSeven: Button
    private lateinit var btnEight: Button
    private lateinit var btnNine: Button
    private lateinit var btnStar: Button
    private lateinit var btnClear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 處理邊到邊的視窗插入
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化 UI 元件
        initViews()

        // 設定按鈕點擊事件
        setButtonClickListeners()
    }

    private fun initViews() {
        // 找到對應的 UI 元件
        txtShow = findViewById(R.id.txtShow)
        btnZero = findViewById(R.id.btnZero)
        btnOne = findViewById(R.id.btnOne)
        btnTwo = findViewById(R.id.btnTwo)
        btnThree = findViewById(R.id.btnThree)
        btnFour = findViewById(R.id.btnFour)
        btnFive = findViewById(R.id.btnFive)
        btnSix = findViewById(R.id.btnSix)
        btnSeven = findViewById(R.id.btnSeven)
        btnEight = findViewById(R.id.btnEight)
        btnNine = findViewById(R.id.btnNine)
        btnStar = findViewById(R.id.btnStar)
        btnClear = findViewById(R.id.btnClear)
    }

    private fun setButtonClickListeners() {
        // 設定所有按鈕共用同一個點擊監聽器
        val myListener = View.OnClickListener { v ->
            val s = txtShow.text.toString()
            when (v.id) {
                R.id.btnZero -> txtShow.text = s + "0"
                R.id.btnOne -> txtShow.text = s + "1"
                R.id.btnTwo -> txtShow.text = s + "2"
                R.id.btnThree -> txtShow.text = s + "3"
                R.id.btnFour -> txtShow.text = s + "4"
                R.id.btnFive -> txtShow.text = s + "5"
                R.id.btnSix -> txtShow.text = s + "6"
                R.id.btnSeven -> txtShow.text = s + "7"
                R.id.btnEight -> txtShow.text = s + "8"
                R.id.btnNine -> txtShow.text = s + "9"
                R.id.btnStar -> txtShow.text = s + "*"
                R.id.btnClear -> txtShow.text = "電話號碼："
            }
        }

        // 為所有按鈕設定監聽器
        btnZero.setOnClickListener(myListener)
        btnOne.setOnClickListener(myListener)
        btnTwo.setOnClickListener(myListener)
        btnThree.setOnClickListener(myListener)
        btnFour.setOnClickListener(myListener)
        btnFive.setOnClickListener(myListener)
        btnSix.setOnClickListener(myListener)
        btnSeven.setOnClickListener(myListener)
        btnEight.setOnClickListener(myListener)
        btnNine.setOnClickListener(myListener)
        btnStar.setOnClickListener(myListener)
        btnClear.setOnClickListener(myListener)
    }
}