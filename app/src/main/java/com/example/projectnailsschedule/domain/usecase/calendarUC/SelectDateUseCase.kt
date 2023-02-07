package com.example.projectnailsschedule.domain.usecase.calendarUC

import android.os.Bundle
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.StatusRepository

class SelectDateUseCase(private val statusRepository: StatusRepository) {

    fun execute(dateParams: DateParams): Bundle {
        // create bundle
        val bundle = Bundle()
        // put dateParams to the bundle
        bundle.putParcelable("dateParams", dateParams)
        return bundle
    }
}