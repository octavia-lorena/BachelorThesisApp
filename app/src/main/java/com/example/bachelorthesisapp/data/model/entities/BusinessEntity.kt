package com.example.bachelorthesisapp.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businesses")
data class BusinessEntity(
    @PrimaryKey(autoGenerate = false)
    override var id: String,
    var businessName: String,
    override var username: String,
    override var email: String,
    override var password: String,
    var businessType: BusinessType,
    var city: String,
    var address: String,
    var lat: Double?,
    var lng: Double?,
    var phoneNumber: String
) : UserEntity()