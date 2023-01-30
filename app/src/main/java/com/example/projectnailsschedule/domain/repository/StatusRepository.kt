package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.DateParams

interface StatusRepository {

    fun addDate(dateParams: DateParams): Boolean

    fun getStatus(dateParams: DateParams): DateParams

    fun setStatus(dateParams: DateParams): Boolean
}