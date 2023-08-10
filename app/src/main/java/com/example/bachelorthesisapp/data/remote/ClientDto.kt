package com.example.bachelorthesisapp.data.remote

import com.example.bachelorthesisapp.data.model.entities.ClientEntity

data class ClientDto(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val profilePicture: String?,
    val deviceToken: String?
)

fun ClientDto.toEntity(): ClientEntity = ClientEntity(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password,
    phoneNumber = phoneNumber,
    profilePicture = profilePicture,
    deviceToken = deviceToken
)
