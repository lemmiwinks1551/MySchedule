package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/***
 * Class model for
 * information about client
 * */

@Parcelize
@Entity(tableName = "clients")
data class ClientModelDb(
    @PrimaryKey(autoGenerate = true)
    val _id: Long? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "phone")
    val phone: String? = null,

    @ColumnInfo(name = "telegram")
    val telegram: String? = null,

    @ColumnInfo(name = "instagram")
    val instagram: String? = null,

    @ColumnInfo(name = "vk")
    val vk: String? = null,

    @ColumnInfo(name = "whatsapp")
    val whatsapp: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "photo")
    val photo: String? = null
) : Parcelable {
    override fun toString(): String {
        return "Client № ${this._id}, name = ${this.name}, " +
                "phone = ${this.phone}, notes = ${this.notes}"
    }
}