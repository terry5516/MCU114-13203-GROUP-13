package com.example.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val id: String = "",
    val title: String = "",
    val description: String = "",  // 添加這個欄位
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val type: String = ""  // 添加這個欄位
) : Parcelable