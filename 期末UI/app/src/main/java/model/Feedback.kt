package com.example.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feedback(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: String = "",
    val userId: String = ""
) : Parcelable