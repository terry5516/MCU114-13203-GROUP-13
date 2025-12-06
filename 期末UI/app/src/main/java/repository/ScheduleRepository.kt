package com.example.ui.repository

import com.example.ui.model.Schedule

class ScheduleRepository {

    fun getSchedules(): List<Schedule> {
        return listOf(
            Schedule(
                id = "1",
                title = "團隊會議",
                description = "每週例行會議",
                date = "2023-12-10",
                time = "10:00",
                location = "會議室A",
                type = "meeting"
            ),
            Schedule(
                id = "2",
                title = "醫生預約",
                description = "年度健康檢查",
                date = "2023-12-12",
                time = "14:30",
                location = "健康中心",
                type = "appointment"
            )
        )
    }

    fun addSchedule(title: String, description: String, date: String, time: String, location: String, type: String): Schedule {
        return Schedule(
            id = System.currentTimeMillis().toString(),
            title = title,
            description = description,
            date = date,
            time = time,
            location = location,
            type = type
        )
    }

    fun updateSchedule(id: String, title: String, description: String, date: String, time: String, location: String, type: String): Schedule {
        return Schedule(
            id = id,
            title = title,
            description = description,
            date = date,
            time = time,
            location = location,
            type = type
        )
    }
}