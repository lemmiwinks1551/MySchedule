package com.example.projectnailsschedule.domain.models

data class ResponseData(
    val country_code: String,
    val country_text: String,
    val dt_start: String,
    val dt_end: String,
    val work_week_type: String,
    val period: String,
    val note: String,
    val days: List<Day>,
    val statistic: Statistic
)

data class Day(
    val date: String,
    val type_id: Int,
    val type_text: String,
    val note: String,
    val week_day: String,
    val working_hours: Int
)

data class Statistic(
    val calendar_days: Int,
    val calendar_days_without_holidays: Int,
    val work_days: Int,
    val weekends: Int,
    val holidays: Int,
    val working_hours: Int
)
