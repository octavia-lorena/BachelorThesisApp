package com.example.bachelorthesisapp.data.repo.auth

import com.example.bachelorthesisapp.data.model.entities.UserEntity
import com.example.bachelorthesisapp.data.model.entities.UserModel
import com.example.bachelorthesisapp.data.remote.Resource
import com.google.firebase.auth.FirebaseUser

interface IAuthRepository {
    suspend fun register(
        email: String,
        password: String,
        userType: String,
        userEntity: UserEntity
    ): Resource<FirebaseUser>
    suspend fun login(email: String, password: String):Resource<UserModel>
    fun isUserLoggedIn(): Boolean
    fun signOut()
    fun getUserId(): String
}