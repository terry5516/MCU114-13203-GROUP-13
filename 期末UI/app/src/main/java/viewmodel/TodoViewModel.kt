package com.example.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ui.model.Todo
import com.example.ui.repository.TodoRepository

class TodoViewModel : ViewModel() {

    private val repository = TodoRepository()
    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todos

    init {
        loadTodos()
    }

    private fun loadTodos() {
        _todos.value = repository.getTodos()
    }

    fun addTodo(title: String, description: String, dueDate: String, priority: String) {
        val newTodo = repository.addTodo(title, description, dueDate, priority)
        val currentTodos = _todos.value?.toMutableList() ?: mutableListOf()
        currentTodos.add(newTodo)
        _todos.value = currentTodos
    }

    fun updateTodo(id: String, title: String, description: String, dueDate: String, priority: String) {
        val updatedTodo = repository.updateTodo(id, title, description, dueDate, priority)
        val currentTodos = _todos.value?.toMutableList() ?: mutableListOf()
        val index = currentTodos.indexOfFirst { it.id == id }
        if (index != -1) {
            currentTodos[index] = updatedTodo
            _todos.value = currentTodos
        }
    }

    fun toggleTodoCompletion(id: String) {
        val currentTodos = _todos.value?.toMutableList() ?: mutableListOf()
        val index = currentTodos.indexOfFirst { it.id == id }
        if (index != -1) {
            val todo = currentTodos[index]
            currentTodos[index] = todo.copy(isCompleted = !todo.isCompleted)
            _todos.value = currentTodos
        }
    }
}