package com.example.bachelorthesisapp.data.model.entities

import androidx.compose.runtime.key
import com.google.firebase.auth.FirebaseUser

open class UserEntity {
    open lateinit var id: String
    open lateinit var email: String
    open lateinit var password: String
    open lateinit var username: String
}

data class UserModel(
    var id: String,
    var email: String?,
    var username: String?,
    var type: String?
)


fun FirebaseUser.toUserModel() = UserModel(
    id = uid,
    email = email,
    username = displayName,
    type = null
)
fun UserModel.toUserEntity() = UserEntity(
)

fun HashMap<String, String>.toUserModel() = UserModel(
    id = get("id")!!,
    email = get("email"),
    username = get("username"),
    type = null
)