package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "calendar")
data class CalendarDateModelDb(
    @PrimaryKey(autoGenerate = true)
    val _id: Int? = null,

    @ColumnInfo(name = "date")
    val date: String? = null,

    @ColumnInfo(name = "color")
    var color: String? = null,

    @ColumnInfo(name = "syncUUID")
    val syncUUID: String? = null,

    @ColumnInfo(name = "userName")
    var userName: String? = null,

    @ColumnInfo(name = "syncTimestamp")
    var syncTimestamp: Long? = null,

    @ColumnInfo(name = "syncStatus")
    var syncStatus: String? = null,
) : Parcelable