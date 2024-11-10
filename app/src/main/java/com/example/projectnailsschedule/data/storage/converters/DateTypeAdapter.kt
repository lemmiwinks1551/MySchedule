package com.example.projectnailsschedule.data.storage.converters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTypeAdapter : TypeAdapter<Date>() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    override fun write(out: JsonWriter?, value: Date?) {
        if (value != null) {
            out?.value(dateFormat.format(value))
        } else {
            out?.nullValue()
        }
    }

    override fun read(`in`: JsonReader?): Date? {
        val dateStr = `in`?.nextString()
        return if (dateStr != null) dateFormat.parse(dateStr) else null
    }
}
