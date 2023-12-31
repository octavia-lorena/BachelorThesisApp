package com.example.bachelorthesisapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.model.BusinessType
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.model.UserModel
import com.example.bachelorthesisapp.data.model.events.BusinessRegisterEvent
import com.example.bachelorthesisapp.data.model.events.ClientRegisterEvent
import com.example.bachelorthesisapp.data.model.events.LoginEvent
import com.example.bachelorthesisapp.data.model.states.BusinessRegisterFormState
import com.example.bachelorthesisapp.data.model.states.ClientRegisterFormState
import com.example.bachelorthesisapp.data.model.states.LoginFormState
import com.example.bachelorthesisapp.data.model.validators.BusinessRegisterFormValidator
import com.example.bachelorthesisapp.data.model.validators.ClientRegisterFormValidator
import com.example.bachelorthesisapp.data.model.validators.LoginFormValidator
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.authentication.AuthRepository
import com.example.bachelorthesisapp.data.authentication.await
import com.example.bachelorthesisapp.data.notifications.FirebaseMessageService
import com.example.bachelorthesisapp.core.presentation.UiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

//    // LOGIN FLOW
//    private var _loginFlow = MutableStateFlow<Resource<UserModel>?>(Resource.Loading())
//    val loginFlow: StateFlow<Resource<UserModel>?> = _loginFlow

    // LOADING STATE
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

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

    val loginState1: Flow<UiState<UserModel>> = authRepository.loginFlow.map { userModel ->
        when (userModel) {
            is Resource.Error -> {
                _isLoading.value = false
                UiState.Error(userModel.cause)
            }

            is Resource.Loading -> {
                _isLoading.value = true
                UiState.Loading
            }

            is Resource.Success -> {
                _isLoading.value = false
                UiState.Success(userModel.value)
            }
        }
    }

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    val userTypeState: Flow<String> = authRepository.userType
    private var _userFlow = MutableStateFlow<UserModel?>(null)
    val userFlow: StateFlow<UserModel?> = _userFlow

    private suspend fun getUserType(id: String) = authRepository.getUserType(id)

    suspend fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                loginState = loginState.copy(email = event.email)
                validateEmailLoginEventForm()
            }

            is LoginEvent.PasswordChanged -> {
                loginState = loginState.copy(password = event.password)
                validatePasswordLoginEventForm()
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
                validateFirstNameClientRegisterEventForm()
            }

            is ClientRegisterEvent.LastNameChanged -> {
                registerClientState = registerClientState.copy(lastName = event.lastName)
                validateLastNameClientRegisterEventForm()
            }

            is ClientRegisterEvent.EmailChanged -> {
                registerClientState = registerClientState.copy(email = event.email)
                validateEmailClientRegisterEventForm()
            }

            is ClientRegisterEvent.PasswordChanged -> {
                registerClientState = registerClientState.copy(password = event.password)
                validatePasswordClientRegisterEventForm()
            }

            is ClientRegisterEvent.PhoneNumberChanged -> {
                registerClientState = registerClientState.copy(phoneNumber = event.phoneNumber)
                validatePhoneNumberClientRegisterEventForm()
            }

            is ClientRegisterEvent.ConfirmPasswordChanged -> {
                registerClientState =
                    registerClientState.copy(confirmPassword = event.confirmPassword)
                validateConfirmPasswordClientRegisterEventForm()
            }

            is ClientRegisterEvent.Submit -> {
                submitClientRegisterForm()
            }
        }
    }

    private fun validateConfirmPasswordClientRegisterEventForm() {
        val result = clientRegisterValidator.validateConfirmPassword(
            registerClientState.password, registerClientState.confirmPassword
        )
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerClientState = result.errorMessage?.let {
                registerClientState.copy(
                    confirmPasswordError = it,
                )
            }!!
            return
        } else {
            registerClientState = registerClientState.copy(
                confirmPasswordError = null,
            )
            return
        }
    }

    private fun validatePhoneNumberClientRegisterEventForm() {
        val result = clientRegisterValidator.validatePhoneNumber(registerClientState.phoneNumber)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerClientState = registerClientState.copy(
                phoneNumberError = result.errorMessage,
            )
            return
        } else {
            registerClientState = registerClientState.copy(
                phoneNumberError = null,
            )
            return
        }
    }

    private fun validatePasswordClientRegisterEventForm() {
        val result = clientRegisterValidator.validatePassword(registerClientState.password)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerClientState = registerClientState.copy(
                passwordError = result.errorMessage,
            )
            return
        } else {
            registerClientState = registerClientState.copy(
                passwordError = null,
            )
            return
        }
    }

    private fun validateEmailClientRegisterEventForm() {
        val result = clientRegisterValidator.validateEmail(registerClientState.email)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerClientState = registerClientState.copy(
                emailError = result.errorMessage,
            )
            return
        } else {
            registerClientState = registerClientState.copy(
                emailError = null,
            )
            return
        }
    }

    private fun validateLastNameClientRegisterEventForm() {
        val result = clientRegisterValidator.validateLastName(registerClientState.lastName)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerClientState = registerClientState.copy(
                lastNameError = result.errorMessage,
            )
            return
        } else {
            registerClientState = registerClientState.copy(
                lastNameError = null,
            )
            return
        }
    }

    private fun validateFirstNameClientRegisterEventForm() {
        val result = clientRegisterValidator.validateFirstName(registerClientState.firstName)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerClientState = registerClientState.copy(
                firstNameError = result.errorMessage,
            )
            return
        } else {
            registerClientState = registerClientState.copy(
                firstNameError = null,
            )
            return
        }
    }

    fun onBusinessRegisterEvent(event: BusinessRegisterEvent) {
        when (event) {
            is BusinessRegisterEvent.NameChanged -> {
                registerBusinessState = registerBusinessState.copy(name = event.name)
                validateNameBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.TypeChanged -> {
                registerBusinessState = registerBusinessState.copy(type = event.type)
                validateTypeBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.PhoneNumberChanged -> {
                registerBusinessState = registerBusinessState.copy(phoneNumber = event.phoneNumber)
                validatePhoneNumberBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.AddressChanged -> {
                registerBusinessState = registerBusinessState.copy(address = event.address)
                validateAddressBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.EmailChanged -> {
                registerBusinessState = registerBusinessState.copy(email = event.email)
                validateEmailBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.PasswordChanged -> {
                registerBusinessState = registerBusinessState.copy(password = event.password)
                validatePasswordBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.ConfirmPasswordChanged -> {
                registerBusinessState =
                    registerBusinessState.copy(confirmPassword = event.confirmPassword)
                validateConfirmPasswordBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.CityChanged -> {
                registerBusinessState = registerBusinessState.copy(city = event.city)
                validateCityBusinessRegisterEventForm()
            }

            is BusinessRegisterEvent.ProfilePictureChanged -> {
                registerBusinessState =
                    registerBusinessState.copy(profilePicture = event.profilePicture)
            }


            is BusinessRegisterEvent.Submit -> {
                submitBusinessRegisterForm()
            }

            is BusinessRegisterEvent.PartialSubmit -> {
                partialSubmitBusinessRegisterForm()
            }

        }
    }

    private fun validateNameBusinessRegisterEventForm() {
        val result = businessRegisterValidator.validateName(registerBusinessState.name)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                nameError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                nameError = null,
            )
            return
        }
    }

    private fun validateTypeBusinessRegisterEventForm() {
        val result = businessRegisterValidator.validateType(registerBusinessState.type)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                typeError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                typeError = null,
            )
            return
        }
    }

    private fun validatePhoneNumberBusinessRegisterEventForm() {
        val result =
            businessRegisterValidator.validatePhoneNumber(registerBusinessState.phoneNumber)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                phoneNumberError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                phoneNumberError = null,
            )
            return
        }
    }

    private fun validateAddressBusinessRegisterEventForm() {
        val result = businessRegisterValidator.validateAddress(registerBusinessState.address)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                addressError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                addressError = null,
            )
            return
        }
    }

    private fun validateEmailBusinessRegisterEventForm() {
        val result = businessRegisterValidator.validateEmail(registerBusinessState.email)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                emailError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                emailError = null,
            )
            return
        }
    }

    private fun validatePasswordBusinessRegisterEventForm() {
        val result = businessRegisterValidator.validatePassword(registerBusinessState.password)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                passwordError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                passwordError = null,
            )
            return
        }
    }

    private fun validateConfirmPasswordBusinessRegisterEventForm() {
        val result = businessRegisterValidator.validateConfirmPassword(
            registerBusinessState.password, registerBusinessState.confirmPassword
        )
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                confirmPasswordError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                confirmPasswordError = null,
            )
            return
        }
    }

    private fun validateCityBusinessRegisterEventForm() {
        val result = businessRegisterValidator.validateCity(registerBusinessState.city)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                cityError = result.errorMessage,
            )
            return
        } else {
            registerBusinessState = registerBusinessState.copy(
                cityError = null,
            )
            return
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
//            loginState1.collect {
//                when (it) {
//                    is UiState.Loading -> {
//                       // _isLoading.value = true
//                    }
//                    is UiState.Success -> {
//                      //  _isLoading.value = false
//                        validationLoginEventChannel.send(ValidationEvent.Success)
//                    }
//
//                    is UiState.Error -> {
//                       // _isLoading.value = false
//                        Log.d("LOGIN", "Error: ${it.cause.message}")
//                        validationLoginEventChannel.send(ValidationEvent.Failure)
//                    }
//                }
//            }
        }
    }


    private fun validateEmailLoginEventForm() {
        val result = loginValidator.validateEmail(loginState.email)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            loginState = loginState.copy(
                emailError = result.errorMessage,
            )
            return
        } else {
            loginState = loginState.copy(
                emailError = null,
            )
            return
        }
    }

    private fun validatePasswordLoginEventForm() {
        val result = loginValidator.validatePassword(loginState.password)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            loginState = loginState.copy(
                passwordError = result.errorMessage,
            )
            return
        } else {
            loginState = loginState.copy(
                passwordError = null,
            )
            return
        }
    }

    private fun submitClientRegisterForm() {
        val firstNameResult =
            clientRegisterValidator.validateFirstName(registerClientState.firstName)
        val lastNameResult = clientRegisterValidator.validateLastName(registerClientState.lastName)
        val emailResult = clientRegisterValidator.validateEmail(registerClientState.email)
        val passwordResult = clientRegisterValidator.validatePassword(registerClientState.password)
        val confirmPasswordResult = clientRegisterValidator.validateConfirmPassword(
            registerClientState.password, registerClientState.confirmPassword
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
            val username =
                registerClientState.firstName.lowercase() + "_" + registerClientState.lastName.lowercase()
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
                username = username,
                profilePicture = "",
                deviceToken = Firebase.messaging.token.await()
            )
            registerClient(user)
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
        val addressResult = businessRegisterValidator.validateAddress(registerBusinessState.address)
        val cityResult = businessRegisterValidator.validateAddress(registerBusinessState.city)

        val hasError = listOf(
            addressResult, cityResult
        ).any { !it.success }
        if (hasError) {
            registerBusinessState = registerBusinessState.copy(
                addressError = addressResult.errorMessage, cityError = cityResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            val email = registerBusinessState.email
            val password = registerBusinessState.password
            val username = registerBusinessState.name.lowercase().split(" ").joinToString("_")
            val name = registerBusinessState.name
            val phoneNumber = registerBusinessState.phoneNumber
            val type = registerBusinessState.type
            val address = registerBusinessState.address
            val businessType = findBusinessType(type)
            val city = registerBusinessState.city
            val profilePictureState = registerBusinessState.profilePicture
            val profilePicture: String? = if (profilePictureState == "") {
                null
            } else profilePictureState
            val user = BusinessEntity(
                id = "new",
                businessName = name,
                businessType = businessType,
                phoneNumber = phoneNumber,
                address = address,
                city = city,
                email = email,
                password = password,
                username = username,
                profilePicture = profilePicture,
                deviceToken = Firebase.messaging.token.await()
            )
            Log.d("REGISTER", user.toString())
            registerBusiness(user)
            delay(4000L)
            validationBusinessRegisterEventChannel.send(ValidationEvent.Success)

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
            )
        }
    }

    private fun partialSubmitBusinessRegisterForm() {
        val nameResult = businessRegisterValidator.validateName(registerBusinessState.name)
        val emailResult = businessRegisterValidator.validateEmail(registerBusinessState.email)
        val passwordResult =
            businessRegisterValidator.validatePassword(registerBusinessState.password)
        val confirmPasswordResult = businessRegisterValidator.validateConfirmPassword(
            registerBusinessState.password, registerBusinessState.confirmPassword
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
            // _loginFlow.value = result
        }
    }

    private fun registerClient(clientEntity: ClientEntity) {
        viewModelScope.launch {
            authRepository.registerClient(clientEntity)
        }
    }

    private fun registerBusiness(businessEntity: BusinessEntity) {
        viewModelScope.launch {
            authRepository.registerBusiness(businessEntity)
        }
    }


    fun signOut() {
        authRepository.signOut()
    }

    fun subscribeToTopic(uid: String) {
        Log.d("TOKEN", FirebaseMessageService.token!!)
        Firebase.messaging.subscribeToTopic("/topics/$uid").addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscribe failed"
            }
        }
    }

    fun clearLoginStateForm() {
        viewModelScope.launch {
            loginState = loginState.copy(
                email = "", emailError = null, password = "", passwordError = null
            )
        }
    }

    fun clearRegisterClientStateForm() {
        viewModelScope.launch {
            registerClientState = registerClientState.copy(
                email = "",
                emailError = null,
                password = "",
                passwordError = null,
                confirmPassword = "",
                confirmPasswordError = null,
                firstName = "",
                firstNameError = null,
                lastName = "",
                lastNameError = null,
                phoneNumber = "",
                phoneNumberError = null
            )
        }
    }

    fun clearRegisterBusinessStateForm() {
        viewModelScope.launch {
            registerBusinessState = registerBusinessState.copy(
                name = "",
                nameError = null,
                type = "",
                typeError = null,
                phoneNumber = "",
                phoneNumberError = null,
                city = "",
                cityError = null,
                address = "",
                addressError = null,
                email = "",
                emailError = null,
                password = "",
                passwordError = null,
                confirmPassword = "",
                confirmPasswordError = null,
            )
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object Failure : ValidationEvent()
    }

}