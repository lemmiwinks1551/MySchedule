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
    val color: String? = null
) : Parcelable