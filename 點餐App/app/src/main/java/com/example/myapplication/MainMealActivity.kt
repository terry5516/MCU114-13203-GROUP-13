package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class MainMealActivity : AppCompatActivity() {

    private val viewModel = OrderViewModel.getInstance()  // 使用單例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_meal)

        setupUI()
    }

    private fun setupUI() {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_main_meal)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm_main_meal)

        btnConfirm.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                viewModel.updateMainMeal(radioButton.text.toString())
                finish() // 返回主畫面
            }
        }
    }
}
