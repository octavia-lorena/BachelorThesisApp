package com.example.bachelorthesisapp.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter : JsonAdapter<LocalDate>(){
    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        value?.let { writer.value(it.format(formatter)) }

    }

    override fun fromJson(reader: JsonReader): LocalDate? {
        return if (reader.peek() != JsonReader.Token.NULL) {
            fromNonNullString(reader.nextString())
        } else {
            reader.nextNull<Any>()
            null
        }    }
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    private fun fromNonNullString(nextString: String) : LocalDate = LocalDate.parse(nextString, formatter)

}