package com.example.bachelorthesisapp.data.authentication

import com.example.bachelorthesisapp.domain.model.UserEntity
import com.example.bachelorthesisapp.domain.model.UserModel
import com.example.bachelorthesisapp.core.resources.Resource
import com.google.firebase.auth.FirebaseUser

interface IAuthRepository {
    suspend fun register(
        email: String,
        password: String,
        userType: String,
        userEntity: UserEntity
    ): Resource<FirebaseUser>
    suspend fun login(email: String, password: String): Resource<UserModel>
    fun isUserLoggedIn(): Boolean
    fun signOut()
    fun getUserId(): String
}