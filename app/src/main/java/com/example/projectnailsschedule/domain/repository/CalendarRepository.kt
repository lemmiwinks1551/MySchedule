package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.DateParams

interface CalendarRepository {

    fun addDate(dateParams: DateParams): Boolean

    fun getDate(dateParams: DateParams): Boolean

    fun editDate(dateParams: DateParams): Boolean
}