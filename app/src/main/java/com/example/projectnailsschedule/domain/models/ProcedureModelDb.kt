package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "procedures")
data class ProcedureModelDb(
    @PrimaryKey(autoGenerate = true)
    val _id: Int? = null,

    @ColumnInfo(name = "procedureName")
    val procedureName: String? = null,

    @ColumnInfo(name = "procedurePrice")
    val procedurePrice: String? = null,

    @ColumnInfo(name = "procedureNotes")
    val procedureNotes: String? = null
) : Parcelable {

    override fun toString(): String {
        return "Procedure № ${this._id}, procedureName = ${this.procedureName}, " +
                "procedurePrice = ${this.procedurePrice}, procedureNotes = ${this.procedureNotes}"
    }
}