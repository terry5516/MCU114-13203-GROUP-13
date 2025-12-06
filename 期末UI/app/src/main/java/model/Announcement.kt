package com.example.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Announcement(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: String = "",
    val author: String = ""
) : Parcelable