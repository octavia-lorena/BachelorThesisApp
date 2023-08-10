package com.example.bachelorthesisapp.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.bachelorthesisapp.data.model.converters.BusinessTypeConverter

@Entity(tableName = "businesses")
@TypeConverters(BusinessTypeConverter::class)
data class BusinessEntity(
    @PrimaryKey(autoGenerate = false)
    override var id: String,
    var businessName: String,
    override var username: String,
    override var email: String,
    override var password: String,
    override var profilePicture: String?,
    var businessType: BusinessType,
    var city: String,
    var address: String,
    var lat: Double?,
    var lng: Double?,
    var phoneNumber: String,
    var deviceToken: String?,
) : UserEntity()