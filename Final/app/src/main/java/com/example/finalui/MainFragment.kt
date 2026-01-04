package com.example.finalui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.FrameLayout
import android.widget.Toast

class MainFragment : Fragment() {

    companion object {
        const val PAGE_HOME = 0
        const val PAGE_SEARCH = 1
        const val PAGE_SCHEDULE = 2
        const val PAGE_PROFILE = 3
    }

    private var currentPage = PAGE_HOME

    private lateinit var layoutHome: LinearLayout
    private lateinit var layoutSearch: LinearLayout
    private lateinit var layoutFavorites: LinearLayout
    private lateinit var layoutProfile: LinearLayout
    private lateinit var btnSchoolWebsite: Button
    private lateinit var webView: WebView
    private lateinit var webviewContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // 綁定各個 Layout
        layoutHome = view.findViewById(R.id.layout_home)
        layoutSearch = view.findViewById(R.id.layout_search)
        layoutFavorites = view.findViewById(R.id.layout_favorites)
        layoutProfile = view.findViewById(R.id.layout_profile)

        // 綁定校網按鈕
        btnSchoolWebsite = view.findViewById(R.id.btn_school_website)

        // 綁定 WebView 容器
        webviewContainer = view.findViewById(R.id.webview_container)

        // 綁定或創建 WebView
        webView = view.findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportZoom(true)

        // 按鈕點擊事件：打開校網
        btnSchoolWebsite.setOnClickListener {
            // 顯示 WebView 容器，隱藏其他搜尋內容
            webviewContainer.visibility = View.VISIBLE
            hideOtherSearchViews()

            // 加載網頁
            webView.loadUrl("https://web2.mcu.edu.tw/")

            // 切換到搜尋頁面
            switchPage(PAGE_SEARCH)

            // 顯示提示
            Toast.makeText(requireContext(), "正在載入學校網站...", Toast.LENGTH_SHORT).show()
        }

        // 顯示初始頁面
        showPage(currentPage)
        return view
    }

    // 隱藏搜尋頁面的其他內容（只顯示 WebView）
    private fun hideOtherSearchViews() {
        val searchViews = listOf(
            R.id.sp_search_category,
            R.id.et_search,
            R.id.btn_search,
            R.id.tv_search_result,
            R.id.list_search_history
        )

        searchViews.forEach { id ->
            view?.findViewById<View>(id)?.visibility = View.GONE
        }
    }

    // 切換頁面
    fun switchPage(page: Int) {
        currentPage = page
        showPage(page)
    }

    // 根據 page 顯示對應 layout
    private fun showPage(page: Int) {
        layoutHome.visibility = if (page == PAGE_HOME) View.VISIBLE else View.GONE
        layoutSearch.visibility = if (page == PAGE_SEARCH) View.VISIBLE else View.GONE
        layoutFavorites.visibility = if (page == PAGE_SCHEDULE) View.VISIBLE else View.GONE
        layoutProfile.visibility = if (page == PAGE_PROFILE) View.VISIBLE else View.GONE
    }
}