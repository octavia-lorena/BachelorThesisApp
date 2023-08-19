package com.example.bachelorthesisapp.data.model.converters

import android.util.Log
import androidx.room.TypeConverter
import com.example.bachelorthesisapp.data.model.BusinessType
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.data.model.EventType
import java.time.LocalDate
import java.util.TreeMap

class EventConverter {

    @TypeConverter
    fun eventTypeToString(eventType: EventType): String = eventType.name

    @TypeConverter
    fun fromVendors(vendors: Map<BusinessType, Int>): String {
        val vendorsStringKey = vendors.map { Pair(it.key.name, it.value) }.toMap()
        val sortedMap: TreeMap<String, Int?> = TreeMap(vendorsStringKey)
        val result = sortedMap.keys.joinToString(separator = ",").plus("<divider>")
            .plus(sortedMap.values.joinToString(separator = ","))
        Log.d("MAP", result)
        return result
    }

    @TypeConverter
    fun toVendors(string: String): Map<BusinessType, Int> {
        val businessTypes = enumValues<BusinessType>()
        if (string.isNotEmpty())
            return string.split("<divider>").run {
                val keys = getOrNull(0)?.split(",")
                    ?.map { value -> businessTypes.first { it.name == value } }

                val values = getOrNull(1)?.split(",")?.map { value ->
                    value.toInt()
                }
                val res = hashMapOf<BusinessType, Int>()
                keys?.forEachIndexed { index, s ->
                    if (values != null) {
                        res[s] = values[index]
                    }
                }
                res
            }
        return emptyMap()
    }

    @TypeConverter
    fun fromDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toDate(string: String): LocalDate = LocalDate.parse(string)

    @TypeConverter
    fun eventStatusToString(status: EventStatus): String = status.name

}