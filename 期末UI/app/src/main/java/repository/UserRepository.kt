package com.example.ui.repository

import com.example.ui.model.User

class UserRepository {

    private val users = mutableListOf<User>()

    init {
        // 初始化測試資料
        users.add(User("1", "張三", "zhangsan@example.com", "0912345678"))
        users.add(User("2", "李四", "lisi@example.com", "0987654321"))
    }

    fun getUsers(): List<User> {
        return users.toList()
    }

    fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }

    fun addUser(user: User): User {
        val newUser = user.copy(id = System.currentTimeMillis().toString())
        users.add(newUser)
        return newUser
    }

    fun updateUser(user: User): User {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
        }
        return user
    }

    fun deleteUser(id: String): Boolean {
        return users.removeIf { it.id == id }
    }
}