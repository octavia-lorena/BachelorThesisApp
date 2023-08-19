package com.example.bachelorthesisapp.data.model

import com.google.firebase.auth.FirebaseUser

open class UserEntity {
    open lateinit var id: String
    open lateinit var email: String
    open lateinit var password: String
    open lateinit var username: String
    open var profilePicture: String? = null
}

data class UserModel(
    var id: String,
    var email: String?,
    var username: String?,
    var type: String?,
    var profilePicture: String?,
    var deviceToken: String?
)


fun FirebaseUser.toUserModel() = UserModel(
    id = uid,
    email = email,
    username = displayName,
    type = null,
    profilePicture = photoUrl.toString(),
    deviceToken = null
)