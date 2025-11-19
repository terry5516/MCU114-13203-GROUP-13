package com.example.lab15

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// 數據類
data class Car(
    val id: Long = 0,
    val brand: String,
    val year: String,
    val price: String
)

class MainActivity : AppCompatActivity() {

    private lateinit var edBrand: EditText
    private lateinit var edYear: EditText
    private lateinit var edPrice: EditText
    private lateinit var btnInsert: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnQuery: Button
    private lateinit var listView: ListView

    // 模擬數據庫
    private val carList = mutableListOf<Car>()
    private lateinit var adapter: ArrayAdapter<String>

    // 追蹤當前選中的位置
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupListView()
        setupClickListeners()

        // 添加一些測試數據
        addTestData()
    }

    private fun initializeViews() {
        edBrand = findViewById(R.id.edBrand)
        edYear = findViewById(R.id.edYear)
        edPrice = findViewById(R.id.edPrice)
        btnInsert = findViewById(R.id.btnInsert)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnQuery = findViewById(R.id.btnQuery)
        listView = findViewById(R.id.listView)
    }

    private fun setupListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        listView.adapter = adapter

        // 點擊列表項目的處理
        listView.setOnItemClickListener { parent, view, position, id ->
            selectedPosition = position
            val selectedCar = carList[position]
            edBrand.setText(selectedCar.brand)
            edYear.setText(selectedCar.year)
            edPrice.setText(selectedCar.price)
            Toast.makeText(this, "已選擇: ${selectedCar.brand} (位置: $position)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        // 新增按鈕
        btnInsert.setOnClickListener {
            val brand = edBrand.text.toString().trim()
            val year = edYear.text.toString().trim()
            val price = edPrice.text.toString().trim()

            if (brand.isEmpty() || year.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "請填寫所有欄位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newCar = Car(
                id = System.currentTimeMillis(),
                brand = brand,
                year = year,
                price = price
            )

            carList.add(newCar)
            updateListView()
            clearInputs()
            selectedPosition = -1 // 重置選中位置
            Toast.makeText(this, "新增成功: $brand", Toast.LENGTH_SHORT).show()
        }

        // 查詢按鈕 - 顯示所有數據
        btnQuery.setOnClickListener {
            if (carList.isEmpty()) {
                Toast.makeText(this, "目前沒有資料，請先新增資料", Toast.LENGTH_SHORT).show()
            } else {
                updateListView()
                Toast.makeText(this, "顯示所有資料 (共 ${carList.size} 筆)", Toast.LENGTH_SHORT).show()
            }
        }

        // 修改按鈕
        btnUpdate.setOnClickListener {
            if (selectedPosition == -1) {
                Toast.makeText(this, "請先從列表中選擇要修改的項目", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val brand = edBrand.text.toString().trim()
            val year = edYear.text.toString().trim()
            val price = edPrice.text.toString().trim()

            if (brand.isEmpty() || year.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "請填寫所有欄位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedCar = Car(
                id = carList[selectedPosition].id,
                brand = brand,
                year = year,
                price = price
            )

            carList[selectedPosition] = updatedCar
            updateListView()
            clearInputs()
            selectedPosition = -1
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show()
        }

        // 刪除按鈕 - 修正版本
        btnDelete.setOnClickListener {
            if (selectedPosition == -1) {
                Toast.makeText(this, "請先從列表中選擇要刪除的項目", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedPosition >= 0 && selectedPosition < carList.size) {
                val deletedCar = carList[selectedPosition]
                carList.removeAt(selectedPosition)
                updateListView()
                clearInputs()
                selectedPosition = -1
                Toast.makeText(this, "已刪除: ${deletedCar.brand}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "選擇的位置無效", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateListView() {
        val displayList = carList.mapIndexed { index, car ->
            "${index + 1}. 廠牌: ${car.brand} | 年份: ${car.year} | 價格: ${car.price}"
        }
        adapter.clear()
        adapter.addAll(displayList)
        adapter.notifyDataSetChanged()
    }

    private fun clearInputs() {
        edBrand.text.clear()
        edYear.text.clear()
        edPrice.text.clear()
    }

    private fun addTestData() {
        // 添加一些測試數據
        carList.add(Car(brand = "Toyota", year = "2020", price = "500000"))
        carList.add(Car(brand = "Honda", year = "2021", price = "450000"))
        carList.add(Car(brand = "BMW", year = "2022", price = "1200000"))
        updateListView()
    }
}