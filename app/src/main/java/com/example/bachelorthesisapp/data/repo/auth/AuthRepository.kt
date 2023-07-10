package com.example.bachelorthesisapp.data.repo.auth

import android.util.Log
import com.example.bachelorthesisapp.data.model.entities.UserEntity
import com.example.bachelorthesisapp.data.model.entities.UserModel
import com.example.bachelorthesisapp.data.model.entities.toUserModel
import com.example.bachelorthesisapp.data.remote.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception
import javax.inject.Inject

class AuthRepository @Inject constructor(
) : IAuthRepository {

    companion object {
        const val CLIENTS_TABLE_NAME = "clients"
        const val BUSINESS_TABLE_NAME = "businesses"
        const val USERS_TABLE_NAME = "users"
    }

    var auth: FirebaseAuth = Firebase.auth
    private var database: DatabaseReference = Firebase.database.getReference(USERS_TABLE_NAME)

    private val _userType = MutableSharedFlow<String>()
    val userType: Flow<String> = _userType

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun login(email: String, password: String): Resource<UserModel> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userModel = result.user?.toUserModel()!!
            val userType = getUserType(userModel.id)
            delay(2000L)
            userModel.type = userType
            Log.d("LOGIN", userModel.toString())
            Resource.Success(userModel)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getUserType(id: String): String {
        var typeFound = false
        var result = database.child(CLIENTS_TABLE_NAME).child(id).get().await()

        if (result.value != null)
            typeFound = true

        if (typeFound)
            return CLIENTS_TABLE_NAME

        result = database.child(BUSINESS_TABLE_NAME).child(id).get().await()
        if (result.value != null)
            return BUSINESS_TABLE_NAME
        return ""
    }

    override suspend fun register(
        email: String,
        password: String,
        userType: String,
        userEntity: UserEntity
    ): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = userEntity.username
            }
            val firebaseUser = result.user
            firebaseUser?.updateProfile(profileUpdates)?.await()
            delay(2000L)
            val firebaseId = firebaseUser?.uid!!
            userEntity.id = firebaseId
            database.child(userType).child(firebaseUser.uid).setValue(userEntity)
            delay(1000L)
            Resource.Success(firebaseUser)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override fun isUserLoggedIn(): Boolean = currentUser != null

    override fun signOut() {
        auth.signOut()
    }

    override fun getUserId(): String = auth.currentUser?.uid!!
}