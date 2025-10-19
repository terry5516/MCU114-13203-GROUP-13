package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val viewModel = OrderViewModel.getInstance()  // 使用單例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        observeOrder()
    }

    private fun setupUI() {
        findViewById<Button>(R.id.btn_main_meal).setOnClickListener {
            startActivity(Intent(this, MainMealActivity::class.java))
        }

        findViewById<Button>(R.id.btn_side_dish).setOnClickListener {
            startActivity(Intent(this, SideDishActivity::class.java))
        }

        findViewById<Button>(R.id.btn_drink).setOnClickListener {
            startActivity(Intent(this, DrinkActivity::class.java))
        }

        findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            startActivity(Intent(this, ConfirmActivity::class.java))
        }
    }

    private fun observeOrder() {
        viewModel.order.observe(this) { order ->
            val tvOrderSummary = findViewById<TextView>(R.id.tv_order_summary)
            val summary = """
                當前訂單:
                主餐: ${order.mainMeal ?: "未選擇"}
                副餐: ${if (order.sideDishes.isNotEmpty()) order.sideDishes.joinToString(", ") else "未選擇"}
                飲料: ${order.drink ?: "未選擇"}
            """.trimIndent()
            tvOrderSummary.text = summary
        }
    }
}