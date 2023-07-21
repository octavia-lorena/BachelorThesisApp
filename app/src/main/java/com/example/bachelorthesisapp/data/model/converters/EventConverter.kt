package com.example.bachelorthesisapp.data.model.converters

import androidx.room.TypeConverter
import com.example.bachelorthesisapp.data.model.entities.BusinessType
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import java.time.LocalDate
import java.util.TreeMap

class EventConverter {

    @TypeConverter
    fun eventTypeToString(eventType: EventType): String = eventType.name

    @TypeConverter
    fun fromVendors(vendors: Map<BusinessType, Int?>): String {
        val sortedMap: TreeMap<BusinessType, Int?> = TreeMap(vendors)
        return sortedMap.keys.joinToString(separator = ",").plus("<divider>")
            .plus(sortedMap.values.joinToString(separator = ","))
    }

    @TypeConverter
    fun toVendors(string: String): Map<BusinessType, Int?> {
        return string.split("<divider>").run {
            val keys = getOrNull(0)?.split(",")
                ?.map { value -> enumValues<BusinessType>().first { it.name == value } }
            val values = getOrNull(1)?.split(",")?.map { it.toInt() }
            val res = hashMapOf<BusinessType, Int?>()
            keys?.forEachIndexed { index, s ->
                res[s] = values?.getOrNull(index)
            }
            res
        }
    }

    @TypeConverter
    fun fromDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toDate(string: String): LocalDate = LocalDate.parse(string)

    @TypeConverter
    fun eventStatusToString(status: EventStatus): String = status.name

}