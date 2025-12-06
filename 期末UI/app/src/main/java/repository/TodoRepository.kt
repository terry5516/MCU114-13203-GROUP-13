package com.example.ui.repository

import com.example.ui.model.Todo

class TodoRepository {

    fun getTodos(): List<Todo> {
        // 模擬資料
        return listOf(
            Todo(
                id = "1",
                title = "完成作業",
                description = "數學作業第5章",
                dueDate = "2023-12-10",
                priority = "high",
                isCompleted = false
            ),
            Todo(
                id = "2",
                title = "購買食材",
                description = "牛奶、麵包、雞蛋",
                dueDate = "2023-12-08",
                priority = "medium",
                isCompleted = true
            )
        )
    }

    fun addTodo(title: String, description: String, dueDate: String, priority: String): Todo {
        return Todo(
            id = System.currentTimeMillis().toString(),
            title = title,
            description = description,
            dueDate = dueDate,
            priority = priority,
            isCompleted = false
        )
    }

    fun updateTodo(id: String, title: String, description: String, dueDate: String, priority: String): Todo {
        return Todo(
            id = id,
            title = title,
            description = description,
            dueDate = dueDate,
            priority = priority,
            isCompleted = false
        )
    }
}