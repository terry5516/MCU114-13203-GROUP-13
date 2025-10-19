package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity : AppCompatActivity() {

    private val viewModel = OrderViewModel.getInstance()  // 使用單例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        setupUI()
        displayOrderSummary()
    }

    private fun setupUI() {
        findViewById<Button>(R.id.btn_submit_order).setOnClickListener {
            validateAndSubmitOrder()
        }

        findViewById<Button>(R.id.btn_back_to_main).setOnClickListener {
            finish()
        }
    }

    private fun displayOrderSummary() {
        val order = viewModel.order.value
        val tvSummary = findViewById<TextView>(R.id.tv_order_summary_detail)

        val summary = buildString {
            append("訂單詳情:\n\n")
            append("主餐: ${order?.mainMeal ?: "未選擇"}\n")
            append("副餐: ${if (order?.sideDishes?.isNotEmpty() == true) order.sideDishes.joinToString(", ") else "未選擇"}\n")
            append("飲料: ${order?.drink ?: "未選擇"}")
        }

        tvSummary.text = summary
    }

    private fun validateAndSubmitOrder() {
        val order = viewModel.order.value

        if (order?.isComplete() != true) {
            Toast.makeText(
                this,
                "請選擇主餐、至少一個副餐和飲料。",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // 顯示確認對話框
        AlertDialog.Builder(this)
            .setTitle("提交訂單")
            .setMessage(
                "主餐: ${order.mainMeal}\n" +
                        "副餐: ${order.sideDishes.joinToString(", ")}\n" +
                        "飲料: ${order.drink}\n\n確認提交此訂單？"
            )
            .setPositiveButton("提交") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "訂單已提交！", Toast.LENGTH_SHORT).show()
                // viewModel.clearOrder() // 先註解這行，測試資料共享
                finish()
            }
            .setNegativeButton("取消", null)
            .show()
    }
}