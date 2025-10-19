package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class SideDishActivity : AppCompatActivity() {

    private val viewModel = OrderViewModel.getInstance()  // 使用單例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_dish)

        setupUI()
    }

    private fun setupUI() {
        val checkBoxFries = findViewById<CheckBox>(R.id.checkbox_fries)
        val checkBoxSalad = findViewById<CheckBox>(R.id.checkbox_salad)
        val checkBoxSoup = findViewById<CheckBox>(R.id.checkbox_soup)
        val checkBoxBread = findViewById<CheckBox>(R.id.checkbox_bread)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm_side_dish)

        btnConfirm.setOnClickListener {
            // 清除之前的選擇
            viewModel.order.value?.sideDishes?.clear()

            // 添加新的選擇
            if (checkBoxFries.isChecked) {
                viewModel.addSideDish(checkBoxFries.text.toString())
            }
            if (checkBoxSalad.isChecked) {
                viewModel.addSideDish(checkBoxSalad.text.toString())
            }
            if (checkBoxSoup.isChecked) {
                viewModel.addSideDish(checkBoxSoup.text.toString())
            }
            if (checkBoxBread.isChecked) {
                viewModel.addSideDish(checkBoxBread.text.toString())
            }

            finish() // 返回主畫面
        }
    }
}