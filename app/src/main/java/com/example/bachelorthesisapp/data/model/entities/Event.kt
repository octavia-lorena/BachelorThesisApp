package com.example.bachelorthesisapp.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.bachelorthesisapp.data.model.converters.EventConverter
import java.time.LocalDate
import java.util.Calendar

@Entity(tableName = "events")
@TypeConverters(EventConverter::class)
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val organizerId: String,
    val name: String,
    val description: String,
    val type: EventType,
    val date: LocalDate,
    val time: String,
    val guestNumber: Int,
    val budget: Int,
    val cost: Int,
    val vendors: Map<BusinessType, Int?>,
    val status: EventStatus
)
