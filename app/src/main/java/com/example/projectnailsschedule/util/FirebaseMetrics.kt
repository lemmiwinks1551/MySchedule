package com.example.projectnailsschedule.util

import android.util.Log
import com.example.projectnailsschedule.domain.models.FirebaseModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseMetrics {
    private val log = this::class.simpleName
    private val key = "scheduleDbMetrics"
    private val firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(key)

    fun insertMetrics(firebaseModel: FirebaseModel) {
        val id = firebaseRef.key
        val firebaseModelInsert = FirebaseModel(
            id = id,
            userId = firebaseModel.userId,
            time = firebaseModel.time,
            event = firebaseModel.event
        )
        Thread {
            try {
                firebaseRef.push().setValue(firebaseModelInsert)
                Log.e(log, "$firebaseModelInsert inserted")
            } catch (e: Exception) {
                Log.e(log, e.message.toString())
            }
        }.start()
    }
}