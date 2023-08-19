package com.example.bachelorthesisapp.data.clients.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorthesisapp.data.model.UserEntity
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
    override var profilePicture: String?,
    val phoneNumber: String?,
    var deviceToken: String?,
) : UserEntity()
