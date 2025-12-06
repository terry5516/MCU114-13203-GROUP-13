package com.example.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ui.model.Schedule
import com.example.ui.repository.ScheduleRepository

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()
    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>> = _schedules

    init {
        loadSchedules()
    }

    private fun loadSchedules() {
        _schedules.value = repository.getSchedules()
    }

    fun addSchedule(title: String, description: String, date: String, time: String, location: String, type: String) {
        val newSchedule = repository.addSchedule(title, description, date, time, location, type)
        val currentSchedules = _schedules.value?.toMutableList() ?: mutableListOf()
        currentSchedules.add(newSchedule)
        _schedules.value = currentSchedules
    }

    fun updateSchedule(id: String, title: String, description: String, date: String, time: String, location: String, type: String) {
        val updatedSchedule = repository.updateSchedule(id, title, description, date, time, location, type)
        val currentSchedules = _schedules.value?.toMutableList() ?: mutableListOf()
        val index = currentSchedules.indexOfFirst { it.id == id }
        if (index != -1) {
            currentSchedules[index] = updatedSchedule
            _schedules.value = currentSchedules
        }
    }
}