package com.example.majorcitytemp

import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : AppCompatActivity() {

    data class WeatherResponse(val main: MainData, val name: String)
    data class MainData(val temp: Double, val humidity: Int)

    // 混合使用城市名稱和城市ID
    private val cityData = listOf(
        Pair("Taipei", null),
        Pair("New Taipei", null),
        Pair("Taoyuan", null),
        Pair("Hsinchu", null),
        Pair("Miaoli", null),
        Pair("Taichung", null),
        Pair("Changhua", null),
        Pair("Nantou", null),
        Pair("Yunlin", null),
        Pair("Chiayi", null),
        Pair("Tainan", null),
        Pair("Kaohsiung", null),
        Pair("Pingtung", null),
        Pair("Yilan", null),
        Pair("Hualien", null),
        Pair("Taitung", null),
        Pair("Keelung", null),
        Pair("Penghu", null),
        Pair("Jinmen", null),  // 使用座標查詢
        Pair("Lianjiang", null)  // 使用座標查詢
    )

    // 替換金門和連江的查詢方式
    private val specialCities = mapOf(
        "Jinmen" to "lat=24.4333&lon=118.3667",  // 金門座標
        "Lianjiang" to "lat=26.1500&lon=119.9333"  // 馬祖座標
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnGetTemp = findViewById<Button>(R.id.btnGetTemp)
        btnGetTemp.setOnClickListener {
            fetchAllWeather()
        }
    }

    private fun fetchAllWeather() {
        val apiKey = "3278507f08f6b898c7e344f874d983c7"
        val client = OkHttpClient()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val deferredResults = cityData.map { (cityName, cityId) ->
                    async {
                        try {
                            // 在這裡使用新的 URL 生成邏輯
                            val url = if (specialCities.containsKey(cityName)) {
                                val coords = specialCities[cityName]
                                "https://api.openweathermap.org/data/2.5/weather?$coords&units=metric&appid=$apiKey"
                            } else if (cityId != null) {
                                "https://api.openweathermap.org/data/2.5/weather?id=$cityId&units=metric&appid=$apiKey"
                            } else {
                                "https://api.openweathermap.org/data/2.5/weather?q=$cityName,TW&units=metric&appid=$apiKey"
                            }

                            val request = Request.Builder()
                                .url(url)
                                .build()

                            val response = client.newCall(request).execute()

                            if (response.isSuccessful) {
                                val responseData = response.body?.string()
                                if (responseData != null) {
                                    val gson = Gson()
                                    val weatherData = gson.fromJson(responseData, WeatherResponse::class.java)
                                    Pair(cityName, weatherData)
                                } else {
                                    Pair(cityName, null)
                                }
                            } else {
                                Pair(cityName, null)
                            }
                        } catch (e: Exception) {
                            Pair(cityName, null)
                        }
                    }
                }

                val results = deferredResults.awaitAll()
                val successResults = results.filter { it.second != null }
                val failedCities = results.filter { it.second == null }.map { it.first }

                val message = StringBuilder()

                successResults.forEach { (cityName, weatherData) ->
                    weatherData?.let {
                        message.append("$cityName:\n")
                        message.append("  Temperature: ${it.main.temp}°C\n")
                        message.append("  Humidity: ${it.main.humidity}%\n\n")
                    }
                }

                if (failedCities.isNotEmpty()) {
                    message.append("Failed to get weather for:\n")
                    failedCities.forEach { cityName ->
                        message.append("$cityName\n")
                    }
                }

                withContext(Dispatchers.Main) {
                    showAlertDialog("Taiwan Major Cities Weather", message.toString())
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showAlertDialog("Error", "Network error: ${e.message}")
                }
            }
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        val scrollView = ScrollView(this).apply {
            setPadding(50, 0, 50, 0)
        }
        val textView = TextView(this).apply {
            text = message
            textSize = 16f
            setTextIsSelectable(true)
        }
        scrollView.addView(textView)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(scrollView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}