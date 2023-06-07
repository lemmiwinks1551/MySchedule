package com.example.projectnailsschedule.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * Class model for
 * information about client
 * */

@Entity(tableName = "clients")
data class ClientModelDb(
    @PrimaryKey(autoGenerate = true)
    val _id: Int? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "phone")
    val phone: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null
) {
    override fun toString(): String {
        return "Client № ${this._id}, name = ${this.name}, " +
                "phone = ${this.phone}, notes = ${this.notes}"
    }
}