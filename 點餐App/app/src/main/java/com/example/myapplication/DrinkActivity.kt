package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class DrinkActivity : AppCompatActivity() {

    private val viewModel = OrderViewModel.getInstance()  // 使用單例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        setupUI()
    }

    private fun setupUI() {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_drink)
        val btnConfirm = findViewById<Button>(R.id.btn_confirm_drink)

        btnConfirm.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                viewModel.updateDrink(radioButton.text.toString())
                finish() // 返回主畫面
            }
        }
    }
}