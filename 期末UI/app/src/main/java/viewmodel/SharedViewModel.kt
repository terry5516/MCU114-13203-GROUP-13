package com.example.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _currentUserName = MutableLiveData<String?>(null)
    private val _currentStudentId = MutableLiveData<String?>(null)
    private val _currentUserEmail = MutableLiveData<String?>(null)

    // 設置當前用戶
    fun setCurrentUser(studentId: String, name: String, email: String) {
        _currentStudentId.value = studentId
        _currentUserName.value = name
        _currentUserEmail.value = email
    }

    // 清除當前用戶
    fun clearCurrentUser() {
        _currentStudentId.value = null
        _currentUserName.value = null
        _currentUserEmail.value = null
    }

    // 獲取用戶資訊
    fun getUserName(): String? = _currentUserName.value
    fun getStudentId(): String? = _currentStudentId.value
    fun getUserEmail(): String? = _currentUserEmail.value

    // LiveData 版本
    val currentUserName: LiveData<String?> = _currentUserName
    val currentStudentId: LiveData<String?> = _currentStudentId
    val currentUserEmail: LiveData<String?> = _currentUserEmail
}