package com.example.bachelorthesisapp.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = false)
    override var id: String,
    val firstName: String,
    val lastName: String,
    override var email: String,
    override var password: String,
    override var username: String,
    val phoneNumber: String,
) : UserEntity()
