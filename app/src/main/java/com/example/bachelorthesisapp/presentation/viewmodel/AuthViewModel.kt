package com.example.bachelorthesisapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessType
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.data.model.entities.UserEntity
import com.example.bachelorthesisapp.data.model.entities.UserModel
import com.example.bachelorthesisapp.data.model.events.BusinessRegisterEvent
import com.example.bachelorthesisapp.data.model.events.ClientRegisterEvent
import com.example.bachelorthesisapp.data.model.events.LoginEvent
import com.example.bachelorthesisapp.data.model.states.BusinessRegisterFormState
import com.example.bachelorthesisapp.data.model.states.ClientRegisterFormState
import com.example.bachelorthesisapp.data.model.states.LoginFormState
import com.example.bachelorthesisapp.data.model.validators.BusinessRegisterFormValidator
import com.example.bachelorthesisapp.data.model.validators.ClientRegisterFormValidator
import com.example.bachelorthesisapp.data.model.validators.LoginFormValidator
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.repo.auth.AuthRepository
import com.example.bachelorthesisapp.data.repo.auth.await
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        const val CLIENTS_TABLE_NAME = "clients"
        const val BUSINESS_TABLE_NAME = "businesses"
        const val USERS_TABLE_NAME = "users"
    }

    private var database: DatabaseReference =
        Firebase.database.getReference(AuthRepository.USERS_TABLE_NAME)


    // LOGIN STATE
    var loginState by mutableStateOf(LoginFormState())
    private val loginValidator: LoginFormValidator = LoginFormValidator()
    private val validationLoginEventChannel = Channel<ValidationEvent>()
    val validationLoginEvents = validationLoginEventChannel.receiveAsFlow()

    // LOGIN FLOW
    private var _loginFlow = MutableStateFlow<Resource<UserModel>?>(Resource.Loading())
    val loginFlow: StateFlow<Resource<UserModel>?> = _loginFlow

    // CLIENT REGISTER STATE
    var registerClientState by mutableStateOf(ClientRegisterFormState())
    private val clientRegisterValidator: ClientRegisterFormValidator = ClientRegisterFormValidator()
    private val validationClientRegisterEventChannel = Channel<ValidationEvent>()
    val validationClientRegisterEvents = validationClientRegisterEventChannel.receiveAsFlow()

    // BUSINESS REGISTER STATE
    var registerBusinessState by mutableStateOf(BusinessRegisterFormState())
    private val businessRegisterValidator: BusinessRegisterFormValidator =
        BusinessRegisterFormValidator()
    private val validationBusinessRegisterEventChannel = Channel<ValidationEvent>()
    val validationBusinessRegisterEvents = validationBusinessRegisterEventChannel.receiveAsFlow()


    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    val userTypeState: Flow<String> =
        authRepository.userType
    private var _userFlow = MutableStateFlow<UserModel?>(null)
    val userFlow: StateFlow<UserModel?> = _userFlow

    private suspend fun getUserType(id: String) = authRepository.getUserType(id)

    suspend fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                loginState = loginState.copy(email = event.email)
            }

            is LoginEvent.PasswordChanged -> {
                loginState = loginState.copy(password = event.password)
            }

            is LoginEvent.Submit -> {
                submitLoginForm()
            }

        }
    }

    fun onClientRegisterEvent(event: ClientRegisterEvent) {
        when (event) {
            is ClientRegisterEvent.FirstNameChanged -> {
                registerClientState = registerClientState.copy(firstName = event.firstName)
            }

            is ClientRegisterEvent.LastNameChanged -> {
                registerClientState = registerClientState.copy(lastName = event.lastName)
            }

            is ClientRegisterEvent.EmailChanged -> {
                registerClientState = registerClientState.copy(email = event.email)
            }

            is ClientRegisterEvent.PasswordChanged -> {
                registerClientState = registerClientState.copy(password = event.password)
            }

            is ClientRegisterEvent.PhoneNumberChanged -> {
                registerClientState = registerClientState.copy(phoneNumber = event.phoneNumber)
            }

            is ClientRegisterEvent.ConfirmPasswordChanged -> {
                registerClientState =
                    registerClientState.copy(confirmPassword = event.confirmPassword)
            }

            is ClientRegisterEvent.Submit -> {
                submitClientRegisterForm()
            }
        }
    }

    fun onBusinessRegisterEvent(event: BusinessRegisterEvent) {
        when (event) {
            is BusinessRegisterEvent.NameChanged -> {
                registerBusinessState = registerBusinessState.copy(name = event.name)
            }

            is BusinessRegisterEvent.TypeChanged -> {
                registerBusinessState = registerBusinessState.copy(type = event.type)
            }

            is BusinessRegisterEvent.PhoneNumberChanged -> {
                registerBusinessState = registerBusinessState.copy(phoneNumber = event.phoneNumber)
            }

            is BusinessRegisterEvent.AddressChanged -> {
                registerBusinessState = registerBusinessState.copy(address = event.address)
            }

            is BusinessRegisterEvent.EmailChanged -> {
                registerBusinessState = registerBusinessState.copy(email = event.email)
            }

            is BusinessRegisterEvent.PasswordChanged -> {
                registerBusinessState = registerBusinessState.copy(password = event.password)
            }

            is BusinessRegisterEvent.ConfirmPasswordChanged -> {
                registerBusinessState =
                    registerBusinessState.copy(confirmPassword = event.confirmPassword)
            }

            is BusinessRegisterEvent.CityChanged -> {
                registerBusinessState =
                    registerBusinessState.copy(city = event.city)
            }

            is BusinessRegisterEvent.LatChanged -> {
                registerBusinessState =
                    registerBusinessState.copy(lat = event.lat)
            }

            is BusinessRegisterEvent.LngChanged -> {
                registerBusinessState =
                    registerBusinessState.copy(lng = event.lng)
            }


            is BusinessRegisterEvent.Submit -> {
                submitBusinessRegisterForm()
            }

            is BusinessRegisterEvent.PartialSubmit -> {
                partialSubmitBusinessRegisterForm()
            }
        }
    }

    private suspend fun submitLoginForm() {
        val emailResult = loginValidator.validateEmail(loginState.email)
        val passwordResult = loginValidator.validatePassword(loginState.password)
        val hasError = listOf(
            emailResult,
            passwordResult,
        ).any { !it.success }
        if (hasError) {
            loginState = loginState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
            )
            return
        }
        val email = loginState.email
        val password = loginState.password

        viewModelScope.launch {
            login(email, password)
            delay(3000L)
            validationLoginEventChannel.send(ValidationEvent.Success)
            loginState = loginState.copy(
                email = "",
                emailError = null,
                password = "",
                passwordError = null
            )
        }
    }

    private fun submitClientRegisterForm() {
        val firstNameResult =
            clientRegisterValidator.validateFirstName(registerClientState.firstName)
        val lastNameResult = clientRegisterValidator.validateLastName(registerClientState.lastName)
        val emailResult = clientRegisterValidator.validateEmail(registerClientState.email)
        val passwordResult = clientRegisterValidator.validatePassword(registerClientState.password)
        val confirmPasswordResult = clientRegisterValidator.validateConfirmPassword(
            registerClientState.password,
            registerClientState.confirmPassword
        )
        val phoneNumberResult =
            clientRegisterValidator.validatePhoneNumber(registerClientState.phoneNumber)
        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            emailResult,
            passwordResult,
            confirmPasswordResult,
            phoneNumberResult
        ).any { !it.success }
        if (hasError) {
            registerClientState = registerClientState.copy(
                firstNameError = firstNameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage,
                phoneNumberError = phoneNumberResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            val email = registerClientState.email
            val password = registerClientState.password
            val username = registerClientState.firstName.lowercase() + "_" +
                    registerClientState.lastName.lowercase()
            val firstName = registerClientState.firstName
            val lastName = registerClientState.lastName
            val phoneNumber = registerClientState.phoneNumber
            val user = ClientEntity(
                id = "new",
                firstName = firstName,
                lastName = lastName,
                email = email,
                phoneNumber = phoneNumber,
                password = password,
                username = username
            )
            register(email, password, CLIENTS_TABLE_NAME, user)
            validationClientRegisterEventChannel.send(ValidationEvent.Success)
            registerClientState = registerClientState.copy(
                firstName = "",
                firstNameError = null,
                lastName = "",
                lastNameError = null,
                email = "",
                emailError = null,
                phoneNumber = "",
                phoneNumberError = null,
                password = "",
                passwordError = null,
                confirmPassword = "",
                confirmPasswordError = null,

                )
        }
    }

    private fun submitBusinessRegisterForm() {
//        val nameResult =
//            businessRegisterValidator.validateName(registerBusinessState.name)
//        val emailResult = businessRegisterValidator.validateEmail(registerBusinessState.email)
//        val passwordResult =
//            businessRegisterValidator.validatePassword(registerBusinessState.password)
//        val confirmPasswordResult = businessRegisterValidator.validateConfirmPassword(
//            registerBusinessState.password,
//            registerBusinessState.confirmPassword
//        )
//        val phoneNumberResult =
//            businessRegisterValidator.validatePhoneNumber(registerBusinessState.phoneNumber)
//        val typeResult = businessRegisterValidator.validateType(registerBusinessState.type)
        val addressResult = businessRegisterValidator.validateAddress(registerBusinessState.address)
        val cityResult = businessRegisterValidator.validateAddress(registerBusinessState.city)

        val hasError = listOf(
//            nameResult,
//            emailResult,
//            typeResult,
//            passwordResult,
//            confirmPasswordResult,
//            phoneNumberResult,
            addressResult,
            cityResult
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
//                nameError = nameResult.errorMessage,
//                typeError = typeResult.errorMessage,
//                emailError = emailResult.errorMessage,
//                passwordError = passwordResult.errorMessage,
//                confirmPasswordError = confirmPasswordResult.errorMessage,
//                phoneNumberError = phoneNumberResult.errorMessage,
                addressError = addressResult.errorMessage,
                cityError = cityResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            val email = registerBusinessState.email
            val password = registerBusinessState.password
            val username = registerBusinessState.name.lowercase()
            val name = registerBusinessState.name
            val phoneNumber = registerBusinessState.phoneNumber
            val type = registerBusinessState.type
            val address = registerBusinessState.address
            val businessType = findBusinessType(type)
            val city = registerBusinessState.city
            val lat = registerBusinessState.lat
            val lng = registerBusinessState.lng
            val latitude: Double?
            val longitude: Double?
            if (lat.isEmpty() || lng.isEmpty()) {
                latitude = null
                longitude = null
            } else {
                latitude = lat.toDouble()
                longitude = lng.toDouble()
            }
            val user =
                BusinessEntity(
                    id = "new",
                    businessName = name,
                    businessType = businessType,
                    phoneNumber = phoneNumber,
                    address = address,
                    city = city,
                    lat = latitude,
                    lng = longitude,
                    email = email,
                    password = password,
                    username = username
                )
            Log.d("REGISTER", user.toString())
            register(email, password, BUSINESS_TABLE_NAME, user)
            delay(4000L)
            validationBusinessRegisterEventChannel.send(ValidationEvent.Success)
//            val businessOwner = BusinessOwnerData(
//                id = uid ?: "",
//                name = name,
//                type = businessType,
//                phoneNumber = phoneNumber,
//                email = email,
//                address = address,
//                lat = lat,
//                lng = lng
//            )
//            Log.d("BUSINESS", businessOwner.toString())
//            authRepository.addBusinessOwner(businessOwner)

            registerBusinessState = registerBusinessState.copy(
                name = "",
                nameError = null,
                type = "",
                typeError = null,
                phoneNumber = "",
                phoneNumberError = null,
                address = "",
                addressError = null,
                email = "",
                emailError = null,
                password = "",
                passwordError = null,
                confirmPassword = "",
                confirmPasswordError = null,
                city = "",
                cityError = null,
                lat = "",
                lng = "",
            )
        }
    }

    private fun partialSubmitBusinessRegisterForm() {
        val nameResult =
            businessRegisterValidator.validateName(registerBusinessState.name)
        val emailResult = businessRegisterValidator.validateEmail(registerBusinessState.email)
        val passwordResult =
            businessRegisterValidator.validatePassword(registerBusinessState.password)
        val confirmPasswordResult = businessRegisterValidator.validateConfirmPassword(
            registerBusinessState.password,
            registerBusinessState.confirmPassword
        )
        val phoneNumberResult =
            businessRegisterValidator.validatePhoneNumber(registerBusinessState.phoneNumber)
        val typeResult = businessRegisterValidator.validateType(registerBusinessState.type)


        val hasError = listOf(
            nameResult,
            emailResult,
            typeResult,
            passwordResult,
            confirmPasswordResult,
            phoneNumberResult,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                nameError = nameResult.errorMessage,
                typeError = typeResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage,
                phoneNumberError = phoneNumberResult.errorMessage,

                )
            return
        }

        viewModelScope.launch {
            validationBusinessRegisterEventChannel.send(ValidationEvent.Success)

        }
    }

    private fun findBusinessType(type: String): BusinessType {
        var businessType: BusinessType = BusinessType.Venue
        enumValues<BusinessType>().forEach { if (type == it.name) businessType = it }
        return businessType
    }


    private fun login(email: String, password: String) {
        viewModelScope.launch {
            //_loginFlow.value = Resource.Loading()
            val result = authRepository.login(email, password)
            delay(1500L)
            Log.d("LOGIN", result.toString())
            _loginFlow.value = result
        }
    }

    private fun register(email: String, password: String, userType: String, user: UserEntity) {
        viewModelScope.launch {
            authRepository.register(email, password, userType, user)
        }
    }

    suspend fun getBusinessById(uid: String) {
        val result = database.child(AuthRepository.BUSINESS_TABLE_NAME).child(uid).get().await()
        val resultValue = result.value as HashMap<*, *>

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object Failure : ValidationEvent()
    }

}