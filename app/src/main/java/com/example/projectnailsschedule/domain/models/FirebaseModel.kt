package com.example.projectnailsschedule.domain.models

class FirebaseModel(
    val id: String? = null,
    val userId: String? = null,
    val event: String? = null
) {
    override fun toString(): String {
        return String.format("User id: $userId, event: $event")
    }
}