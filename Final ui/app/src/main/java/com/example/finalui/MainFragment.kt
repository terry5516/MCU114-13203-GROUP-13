package com.example.finalui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class User(
    val username: String,
    val email: String,
    val password: String,
    val registerDate: String
)

data class Course(
    val dayOfWeek: String,
    val time: String,
    val name: String,
    val teacher: String,
    val classroom: String
)

class MainFragment : Fragment() {

    companion object {
        const val PAGE_HOME = 0
        const val PAGE_SEARCH = 1
        const val PAGE_SCHEDULE = 2
        const val PAGE_PROFILE = 3
    }

    private lateinit var titleText: TextView

    private lateinit var layoutHome: View
    private lateinit var layoutSearch: View
    private lateinit var layoutFavorites: View
    private lateinit var layoutProfile: View

    // 首頁元素
    private lateinit var tvDateTime: TextView
    private lateinit var btnQuickAction1: Button
    private lateinit var btnQuickAction2: Button
    private lateinit var notificationBadge: TextView
    private lateinit var listQuickTasks: ListView

    // 登入相關元素
    private lateinit var tvLoginStatus: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var layoutLoggedIn: View
    private lateinit var tvLoggedInName: TextView
    private lateinit var tvLoggedInEmail: TextView

    // 搜尋頁元素
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var tvSearchResult: TextView
    private lateinit var listSearchHistory: ListView
    private lateinit var spSearchCategory: Spinner
    private val searchHistory = ArrayList<String>()

    // 課表頁元素
    private lateinit var tvScheduleDate: TextView
    private lateinit var tvTodayTitle: TextView
    private lateinit var tvTodayCourses: TextView
    private lateinit var layoutTodayCourses: LinearLayout

    private lateinit var btnMonday: Button
    private lateinit var btnTuesday: Button
    private lateinit var btnWednesday: Button
    private lateinit var btnThursday: Button
    private lateinit var btnFriday: Button
    private lateinit var btnSaturday: Button

    private lateinit var listFullSchedule: ListView
    private lateinit var scheduleAdapter: ArrayAdapter<String>

    // 個人頁元素
    private lateinit var btnSettings: Button
    private lateinit var btnLogout: Button
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var switchDarkMode: Switch
    private lateinit var btnClearCache: Button
    private lateinit var btnAbout: Button
    private lateinit var btnFeedback: Button // 新增：意見回饋按鈕
    private lateinit var btnClearFeedback: Button // 新增：清除回饋按鈕

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var tvProfileRegDate: TextView

    private var currentUser: User? = null
    private var notificationCount = 3
    private var currentPage = PAGE_HOME

    private val courseSchedule = ArrayList<Course>()
    private val feedbackList = ArrayList<String>() // 新增：意見回饋列表

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        initViews(view)
        setupClickListeners()
        setupAdapters()
        initCourseSchedule()
        loadFeedbackHistory() // 載入意見回饋歷史
        updateLoginUI()
        switchPage(PAGE_HOME)
        return view
    }

    private fun initViews(view: View) {
        titleText = view.findViewById(R.id.title_text)

        layoutHome = view.findViewById(R.id.layout_home)
        layoutSearch = view.findViewById(R.id.layout_search)
        layoutFavorites = view.findViewById(R.id.layout_favorites)
        layoutProfile = view.findViewById(R.id.layout_profile)

        // 首頁元素
        tvDateTime = view.findViewById(R.id.tv_date_time)
        btnQuickAction1 = view.findViewById(R.id.btn_quick_action1)
        btnQuickAction2 = view.findViewById(R.id.btn_quick_action2)
        notificationBadge = view.findViewById(R.id.notification_badge)
        listQuickTasks = view.findViewById(R.id.list_quick_tasks)

        // 登入相關元素
        tvLoginStatus = view.findViewById(R.id.tv_login_status)
        btnLogin = view.findViewById(R.id.btn_login)
        btnRegister = view.findViewById(R.id.btn_register)
        layoutLoggedIn = view.findViewById(R.id.layout_logged_in)
        tvLoggedInName = view.findViewById(R.id.tv_logged_in_name)
        tvLoggedInEmail = view.findViewById(R.id.tv_logged_in_email)

        // 搜尋頁元素
        etSearch = view.findViewById(R.id.et_search)
        btnSearch = view.findViewById(R.id.btn_search)
        tvSearchResult = view.findViewById(R.id.tv_search_result)
        listSearchHistory = view.findViewById(R.id.list_search_history)
        spSearchCategory = view.findViewById(R.id.sp_search_category)

        // 課表頁元素
        tvScheduleDate = view.findViewById(R.id.tv_schedule_date)
        tvTodayTitle = view.findViewById(R.id.tv_today_title)
        tvTodayCourses = view.findViewById(R.id.tv_today_courses)
        layoutTodayCourses = view.findViewById(R.id.layout_today_courses)

        btnMonday = view.findViewById(R.id.btn_monday)
        btnTuesday = view.findViewById(R.id.btn_tuesday)
        btnWednesday = view.findViewById(R.id.btn_wednesday)
        btnThursday = view.findViewById(R.id.btn_thursday)
        btnFriday = view.findViewById(R.id.btn_friday)
        btnSaturday = view.findViewById(R.id.btn_saturday)

        listFullSchedule = view.findViewById(R.id.list_full_schedule)

        // 個人頁元素
        btnSettings = view.findViewById(R.id.btn_settings)
        btnLogout = view.findViewById(R.id.btn_logout)
        tvUserName = view.findViewById(R.id.tv_user_name)
        tvUserEmail = view.findViewById(R.id.tv_user_email)
        switchDarkMode = view.findViewById(R.id.switch_dark_mode)
        btnClearCache = view.findViewById(R.id.btn_clear_cache)
        btnAbout = view.findViewById(R.id.btn_about)
        btnFeedback = view.findViewById(R.id.btn_feedback) // 意見回饋按鈕
        btnClearFeedback = view.findViewById(R.id.btn_clear_feedback) // 清除回饋按鈕

        tvProfileName = view.findViewById(R.id.tv_profile_name)
        tvProfileEmail = view.findViewById(R.id.tv_profile_email)
        tvProfileRegDate = view.findViewById(R.id.tv_profile_reg_date)
    }

    private fun initCourseSchedule() {
        courseSchedule.clear()

        // 星期一課程
        courseSchedule.add(Course("星期一", "08:10-10:00", "行動應用開發", "王老師", "資電234"))
        courseSchedule.add(Course("星期一", "10:10-12:00", "資料庫系統", "李老師", "資電235"))
        courseSchedule.add(Course("星期一", "13:10-15:00", "網路概論", "張老師", "資電236"))

        // 星期二課程
        courseSchedule.add(Course("星期二", "09:10-11:00", "演算法", "陳老師", "資電237"))
        courseSchedule.add(Course("星期二", "14:10-16:00", "網頁設計", "林老師", "資電238"))

        // 星期三課程
        courseSchedule.add(Course("星期三", "08:10-10:00", "作業系統", "吳老師", "資電239"))
        courseSchedule.add(Course("星期三", "10:10-12:00", "軟體工程", "黃老師", "資電240"))

        // 星期四課程
        courseSchedule.add(Course("星期四", "09:10-11:00", "機器學習", "劉老師", "資電241"))
        courseSchedule.add(Course("星期四", "13:10-15:00", "計算機結構", "趙老師", "資電242"))

        // 星期五課程
        courseSchedule.add(Course("星期五", "08:10-10:00", "專題討論", "周老師", "資電243"))
        courseSchedule.add(Course("星期五", "10:10-12:00", "人工智慧", "孫老師", "資電244"))

        // 星期六課程
        courseSchedule.add(Course("星期六", "09:00-12:00", "專題實作", "鄭老師", "資電245"))

        updateFullScheduleList()
    }

    private fun updateFullScheduleList() {
        val scheduleItems = ArrayList<String>()

        val daysOrder = listOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")

        for (day in daysOrder) {
            val dayCourses = courseSchedule.filter { it.dayOfWeek == day }
            if (dayCourses.isNotEmpty()) {
                scheduleItems.add("=== $day ===")
                dayCourses.sortedBy { it.time }.forEach { course ->
                    scheduleItems.add("${course.time} - ${course.name}")
                    scheduleItems.add("  教師: ${course.teacher} | 教室: ${course.classroom}")
                }
            }
        }

        scheduleAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            scheduleItems
        )
        listFullSchedule.adapter = scheduleAdapter
    }

    private fun setupAdapters() {
        val historyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            searchHistory
        )
        listSearchHistory.adapter = historyAdapter

        val categories = arrayOf("全部", "標題", "內容", "標籤", "日期")
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSearchCategory.adapter = categoryAdapter

        val quickTasks = arrayOf(
            "檢查郵件",
            "更新個人資料",
            "查看通知",
            "設置提醒",
            "備份數據"
        )
        val tasksAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            quickTasks
        )
        listQuickTasks.adapter = tasksAdapter
    }

    private fun setupClickListeners() {
        btnQuickAction1.setOnClickListener {
            showToast("快速動作1執行")
            updateDateTime()
        }

        btnQuickAction2.setOnClickListener {
            showToast("快速動作2執行")
            clearNotifications()
        }

        listQuickTasks.setOnItemClickListener { _, _, position, _ ->
            val tasks = arrayOf("檢查郵件", "更新個人資料", "查看通知", "設置提醒", "備份數據")
            showToast("執行任務: ${tasks[position]}")
        }

        notificationBadge.setOnClickListener {
            showToast("您有 $notificationCount 個通知")
            clearNotifications()
        }

        btnLogin.setOnClickListener {
            if (currentUser != null) {
                showLoginDialog()
            } else {
                showLoginDialog()
            }
        }

        btnRegister.setOnClickListener {
            if (currentUser != null) {
                logoutUser()
            } else {
                showRegisterDialog()
            }
        }

        btnSearch.setOnClickListener {
            performSearch()
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        listSearchHistory.setOnItemClickListener { _, _, position, _ ->
            etSearch.setText(searchHistory[position])
            performSearch()
        }

        btnMonday.setOnClickListener { showCoursesForDay("星期一") }
        btnTuesday.setOnClickListener { showCoursesForDay("星期二") }
        btnWednesday.setOnClickListener { showCoursesForDay("星期三") }
        btnThursday.setOnClickListener { showCoursesForDay("星期四") }
        btnFriday.setOnClickListener { showCoursesForDay("星期五") }
        btnSaturday.setOnClickListener { showCoursesForDay("星期六") }

        btnSettings.setOnClickListener {
            showToast("打開設定頁面")
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "深色模式已開啟" else "深色模式已關閉")
        }

        btnClearCache.setOnClickListener {
            clearAppCache()
        }

        btnAbout.setOnClickListener {
            showAboutDialog()
        }

        // 新增：意見回饋按鈕點擊事件
        btnFeedback.setOnClickListener {
            showFeedbackDialog()
        }

        // 新增：清除回饋記錄按鈕點擊事件
        btnClearFeedback.setOnClickListener {
            showClearFeedbackDialog()
        }
    }

    private fun updateScheduleDisplay() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val todayChinese = when (dayOfWeek) {
            Calendar.SUNDAY -> "星期日"
            Calendar.MONDAY -> "星期一"
            Calendar.TUESDAY -> "星期二"
            Calendar.WEDNESDAY -> "星期三"
            Calendar.THURSDAY -> "星期四"
            Calendar.FRIDAY -> "星期五"
            Calendar.SATURDAY -> "星期六"
            else -> "未知"
        }

        val formatter = SimpleDateFormat("yyyy年MM月dd日", Locale.TAIWAN)
        val dateStr = formatter.format(Date())
        tvScheduleDate.text = "今天是 $dateStr ($todayChinese)"

        val todayCourses = courseSchedule.filter { it.dayOfWeek == todayChinese }

        if (todayCourses.isNotEmpty()) {
            tvTodayTitle.text = "今日課程 ($todayChinese)"

            val coursesText = StringBuilder()
            todayCourses.sortedBy { it.time }.forEach { course ->
                coursesText.append("⏰ ${course.time}\n")
                coursesText.append("📚 ${course.name}\n")
                coursesText.append("👨‍🏫 ${course.teacher}\n")
                coursesText.append("🏫 ${course.classroom}\n\n")
            }

            tvTodayCourses.text = coursesText.toString()
            layoutTodayCourses.setBackgroundResource(R.drawable.schedule_background)
        } else {
            tvTodayTitle.text = "今日課程 ($todayChinese)"
            tvTodayCourses.text = "今天沒有課程安排，好好休息一下吧！🎉"
            layoutTodayCourses.setBackgroundResource(R.drawable.no_course_background)
        }

        updateDayButtonStyle(todayChinese)
    }

    private fun showCoursesForDay(day: String) {
        val courses = courseSchedule.filter { it.dayOfWeek == day }

        val dialogMessage = if (courses.isNotEmpty()) {
            val builder = StringBuilder()
            builder.append("$day 的課程：\n\n")

            courses.sortedBy { it.time }.forEach { course ->
                builder.append("⏰ ${course.time}\n")
                builder.append("📚 ${course.name}\n")
                builder.append("👨‍🏫 ${course.teacher}\n")
                builder.append("🏫 ${course.classroom}\n\n")
            }
            builder.toString()
        } else {
            "$day 沒有安排課程"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("$day 課表")
            .setMessage(dialogMessage)
            .setPositiveButton("確定", null)
            .show()

        updateDayButtonStyle(day)
    }

    private fun updateDayButtonStyle(selectedDay: String) {
        val dayButtons = mapOf(
            "星期一" to btnMonday,
            "星期二" to btnTuesday,
            "星期三" to btnWednesday,
            "星期四" to btnThursday,
            "星期五" to btnFriday,
            "星期六" to btnSaturday
        )

        dayButtons.forEach { (day, button) ->
            button.elevation = if (day == selectedDay) 8f else 2f
        }
    }

    private fun showLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_login, null)
        val etUsername = dialogView.findViewById<EditText>(R.id.et_username)
        val etPassword = dialogView.findViewById<EditText>(R.id.et_password)
        val tvErrorMessage = dialogView.findViewById<TextView>(R.id.tv_error_message)
        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)

        tvDialogTitle.text = "登入"
        etEmail.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("登入") { dialogInterface, _ ->
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()

                // 修改這裡：只需要有輸入帳號就可以登入
                if (username.isEmpty()) {
                    tvErrorMessage.text = "請輸入帳號"
                    tvErrorMessage.visibility = View.VISIBLE
                    return@setPositiveButton
                }

                // 只要有帳號就給過，不檢查密碼，也不限定要叫 "admin"
                val user = User(
                    username = username,
                    email = "$username@example.com",
                    password = if (password.isNotEmpty()) password else "123456", // 預設密碼
                    registerDate = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN).format(Date())
                )
                loginUser(user)
                showToast("歡迎，$username！")
                dialogInterface.dismiss()
            }
            .setNegativeButton("取消", null)
            .create()

        dialog.show()
    }

    private fun showRegisterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_login, null)
        val etUsername = dialogView.findViewById<EditText>(R.id.et_username)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)
        val etPassword = dialogView.findViewById<EditText>(R.id.et_password)
        val tvErrorMessage = dialogView.findViewById<TextView>(R.id.tv_error_message)
        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialog_title)

        tvDialogTitle.text = "註冊"
        etEmail.visibility = View.VISIBLE

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("註冊") { dialogInterface, _ ->
                val username = etUsername.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    tvErrorMessage.text = "請填寫所有欄位"
                    tvErrorMessage.visibility = View.VISIBLE
                    return@setPositiveButton
                }

                if (password.length < 6) {
                    tvErrorMessage.text = "密碼至少需要6個字符"
                    tvErrorMessage.visibility = View.VISIBLE
                    return@setPositiveButton
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tvErrorMessage.text = "請輸入有效的電子郵件"
                    tvErrorMessage.visibility = View.VISIBLE
                    return@setPositiveButton
                }

                val user = User(
                    username = username,
                    email = email,
                    password = password,
                    registerDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(Date())
                )
                loginUser(user)
                showToast("註冊成功！")
                dialogInterface.dismiss()
            }
            .setNegativeButton("取消", null)
            .create()

        dialog.show()
    }

    // ============= 新增：意見回饋相關功能 =============

    private fun showFeedbackDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_feedback, null)
        val etFeedback = dialogView.findViewById<EditText>(R.id.et_feedback)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.rating_bar)
        val spFeedbackType = dialogView.findViewById<Spinner>(R.id.sp_feedback_type)

        // 設定回饋類型選項
        val feedbackTypes = arrayOf("建議", "錯誤回報", "功能請求", "其他")
        val typeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            feedbackTypes
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFeedbackType.adapter = typeAdapter

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("意見回饋")
            .setView(dialogView)
            .setPositiveButton("提交") { dialogInterface, _ ->
                val feedback = etFeedback.text.toString().trim()
                val rating = ratingBar.rating
                val type = spFeedbackType.selectedItem.toString()

                if (feedback.isEmpty()) {
                    showToast("請輸入您的意見")
                    return@setPositiveButton
                }

                // 儲存回饋
                saveFeedback(feedback, rating, type)
                showToast("感謝您的回饋！")
                dialogInterface.dismiss()
            }
            .setNegativeButton("取消", null)
            .setNeutralButton("查看歷史") { _, _ ->
                showFeedbackHistory()
            }
            .create()

        dialog.show()
    }

    private fun saveFeedback(feedback: String, rating: Float, type: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TAIWAN).format(Date())
        val user = currentUser?.username ?: "匿名用戶"

        val feedbackItem = """
            ⭐ ${"★".repeat(rating.toInt())}${"☆".repeat(5 - rating.toInt())}
            📝 $feedback
            🏷️ 類型：$type
            👤 用戶：$user
            📅 時間：$timestamp
        """.trimIndent()

        feedbackList.add(0, feedbackItem)  // 新的加到最前面
        if (feedbackList.size > 10) {
            feedbackList.removeAt(feedbackList.size - 1)  // 只保留最近的10條
        }

        // 儲存到 SharedPreferences
        saveFeedbackToPrefs()
    }

    private fun showFeedbackHistory() {
        if (feedbackList.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("回饋歷史")
                .setMessage("目前還沒有任何回饋記錄")
                .setPositiveButton("確定", null)
                .show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_feedback_history, null)
        val listView = dialogView.findViewById<ListView>(R.id.list_feedback_history)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            feedbackList
        )
        listView.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle("回饋歷史 (${feedbackList.size} 條)")
            .setView(dialogView)
            .setPositiveButton("關閉", null)
            .create()
            .show()
    }

    private fun saveFeedbackToPrefs() {
        val prefs = requireContext().getSharedPreferences("feedback", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val jsonArray = JSONArray()
        feedbackList.forEach { jsonArray.put(it) }

        editor.putString("feedback_list", jsonArray.toString())
        editor.apply()
    }

    private fun loadFeedbackHistory() {
        val prefs = requireContext().getSharedPreferences("feedback", Context.MODE_PRIVATE)
        val jsonString = prefs.getString("feedback_list", null)

        if (jsonString != null) {
            try {
                val jsonArray = JSONArray(jsonString)
                feedbackList.clear()
                for (i in 0 until jsonArray.length()) {
                    feedbackList.add(jsonArray.getString(i))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showClearFeedbackDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("確認清除")
            .setMessage("確定要清除所有回饋記錄嗎？")
            .setPositiveButton("清除") { _, _ ->
                feedbackList.clear()
                saveFeedbackToPrefs()
                showToast("回饋記錄已清除")
            }
            .setNegativeButton("取消", null)
            .show()
    }

    // ============= 意見回饋功能結束 =============

    private fun loginUser(user: User) {
        currentUser = user
        updateLoginUI()
        showToast("歡迎回來，${user.username}！")
    }

    private fun logoutUser() {
        currentUser = null
        updateLoginUI()
        showToast("已登出")
    }

    private fun updateLoginUI() {
        val isLoggedIn = currentUser != null

        if (isLoggedIn) {
            tvLoginStatus.text = "目前狀態: 已登入"
            tvLoginStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))

            layoutLoggedIn.visibility = View.VISIBLE
            tvLoggedInName.text = "用戶名稱: ${currentUser!!.username}"
            tvLoggedInEmail.text = "電子郵件: ${currentUser!!.email}"

            btnLogin.text = "切換帳號"
            btnRegister.text = "登出"

            updateProfileInfo()
        } else {
            tvLoginStatus.text = "目前狀態: 未登入"
            tvLoginStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))

            layoutLoggedIn.visibility = View.GONE
            btnLogin.text = "登入"
            btnRegister.text = "註冊"

            tvProfileName.text = "未登入"
            tvProfileEmail.text = "未登入"
            tvProfileRegDate.text = "未登入"
            tvUserName.text = "遊客"
            tvUserEmail.text = "未登入"
        }
    }

    private fun updateDateTime() {
        val currentTime = System.currentTimeMillis()
        val formatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.TAIWAN)
        val dateTime = formatter.format(Date(currentTime))
        tvDateTime.text = "當前時間: $dateTime"
    }

    private fun clearNotifications() {
        notificationCount = 0
        updateNotificationBadge()
        showToast("通知已清空")
    }

    private fun updateNotificationBadge() {
        notificationBadge.text = notificationCount.toString()
        notificationBadge.visibility = if (notificationCount > 0) View.VISIBLE else View.GONE
    }

    private fun performSearch() {
        val keyword = etSearch.text.toString().trim()
        val category = spSearchCategory.selectedItem.toString()

        if (keyword.isEmpty()) {
            showToast("請輸入搜尋關鍵字")
            return
        }

        if (!searchHistory.contains(keyword)) {
            searchHistory.add(0, keyword)
            if (searchHistory.size > 5) {
                searchHistory.removeAt(searchHistory.size - 1)
            }
            (listSearchHistory.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        }

        val results = listOf(
            "找到 3 個相關結果",
            "1. 關於 \"$keyword\" 的資訊",
            "2. 在 $category 類別中找到匹配項目",
            "3. 相關資料連結"
        )

        tvSearchResult.text = results.joinToString("\n")
        showToast("在 $category 中搜尋: $keyword")
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("確認登出")
            .setMessage("確定要登出帳號嗎？")
            .setPositiveButton("登出") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun clearAppCache() {
        searchHistory.clear()
        (listSearchHistory.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        showToast("緩存已清除")
    }

    private fun showAboutDialog() {
        val userInfo = if (currentUser != null) {
            "當前用戶: ${currentUser!!.username}\n"
        } else {
            "當前用戶: 未登入\n"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("關於應用")
            .setMessage("Final UI v1.0\n\n$userInfo\n這是一個示範應用程式\n包含課表功能、用戶管理等\n\n開發者: 你的名字")
            .setPositiveButton("確定", null)
            .show()
    }

    private fun updateProfileInfo() {
        if (currentUser != null) {
            tvProfileName.text = currentUser!!.username
            tvProfileEmail.text = currentUser!!.email
            tvProfileRegDate.text = currentUser!!.registerDate
            tvUserName.text = currentUser!!.username
            tvUserEmail.text = currentUser!!.email
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun switchPage(pageIndex: Int) {
        currentPage = pageIndex

        layoutHome.visibility = View.GONE
        layoutSearch.visibility = View.GONE
        layoutFavorites.visibility = View.GONE
        layoutProfile.visibility = View.GONE

        when (pageIndex) {
            PAGE_HOME -> {
                layoutHome.visibility = View.VISIBLE
                titleText.text = "首頁"
                updateDateTime()
                updateNotificationBadge()
            }
            PAGE_SEARCH -> {
                layoutSearch.visibility = View.VISIBLE
                titleText.text = "搜尋"
                etSearch.requestFocus()
            }
            PAGE_SCHEDULE -> {
                layoutFavorites.visibility = View.VISIBLE
                titleText.text = "課表"
                updateScheduleDisplay()
            }
            PAGE_PROFILE -> {
                layoutProfile.visibility = View.VISIBLE
                titleText.text = "個人資料"
                updateProfileInfo()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentPage == PAGE_HOME) {
            updateDateTime()
        } else if (currentPage == PAGE_SCHEDULE) {
            updateScheduleDisplay()
        }
    }
}