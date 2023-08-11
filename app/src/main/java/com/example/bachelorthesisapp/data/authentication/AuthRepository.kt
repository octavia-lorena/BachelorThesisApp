package com.example.bachelorthesisapp.data.authentication

import android.util.Log
import com.example.bachelorthesisapp.data.businesses.remote.BusinessRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.clients.remote.ClientRemoteDataSourceImpl
import com.example.bachelorthesisapp.domain.model.UserEntity
import com.example.bachelorthesisapp.domain.model.UserModel
import com.example.bachelorthesisapp.domain.model.toUserModel
import com.example.bachelorthesisapp.core.resources.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val businessRemoteDataSource: BusinessRemoteDataSourceImpl,
    private val clientRemoteDataSource: ClientRemoteDataSourceImpl
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

    private val _loginFlow = MutableSharedFlow<Resource<UserModel>>()
    val loginFlow: Flow<Resource<UserModel>> = _loginFlow

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun login(email: String, password: String): Resource<UserModel> {
        return try {
            //  _loginFlow.emit(Resource.Loading())
            val result = auth.signInWithEmailAndPassword(email, password).await()
            delay(1000L)
            val userModel = result.user?.toUserModel()!!
            val userType = getUserType(userModel.id)
            delay(2000L)
            userModel.type = userType
            // Updating the user's device token
            val deviceToken = Firebase.messaging.token.await()
            userModel.deviceToken = deviceToken
            when (userModel.type) {
                "clients" -> clientRemoteDataSource.updateClientDeviceToken(
                    userModel.id,
                    deviceToken
                )

                "businesses" -> businessRemoteDataSource.updateBusinessDeviceToken(
                    userModel.id,
                    deviceToken
                )

            }

            delay(3000L)
            Log.d("LOGIN", userModel.toString())
            _loginFlow.emit(Resource.Success(userModel))
            Resource.Success(userModel)
        } catch (e: Exception) {
            _loginFlow.emit(Resource.Error(e))
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