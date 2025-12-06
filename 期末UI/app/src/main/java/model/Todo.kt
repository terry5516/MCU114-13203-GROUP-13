package com.example.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val priority: String = "",  // 添加這個欄位
    val isCompleted: Boolean = false
) : Parcelable