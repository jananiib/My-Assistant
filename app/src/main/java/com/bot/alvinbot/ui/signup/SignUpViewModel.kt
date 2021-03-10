package com.bot.alvinbot.ui.signup

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bot.alvinbot.data.model.User
import com.bot.alvinbot.data.network.Resource
import com.bot.alvinbot.data.network.Status
import com.bot.alvinbot.extensions.isNullOrEmpty
import com.bot.alvinbot.extensions.isValidEmail
import com.bot.alvinbot.extensions.showWarningMessage
import com.bot.alvinbot.repo.AuthRepository
import com.bot.alvinbot.utils.NetworkUtils
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    private val _apiResponse = MutableLiveData<Resource<AuthResult>>()
    val apiResponse: MutableLiveData<Resource<AuthResult>> = _apiResponse


    private val _apiResponseUser = MutableLiveData<Resource<Void>>()
    val apiResponseUser: MutableLiveData<Resource<Void>> = _apiResponseUser


    var firstName = ObservableField("")
    var lastName = ObservableField("")
    var emailId = ObservableField("")
    var emergencyNumber = ObservableField("")
    var password = ObservableField("")
    var confirmPassword = ObservableField("")
    var maleOfFemale = ObservableField(true)

    var firstNameError = ObservableField("")
    var lastNameError = ObservableField("")
    var emailIdError = ObservableField("")
    var emergencyNumberError = ObservableField("")
    var passwordError = ObservableField("")
    var confirmPasswordError = ObservableField("")


    fun validation() {
        clearError()

        if (!isNullOrEmpty(firstName.get())) {
            firstNameError.set("Please enter first name")
        }

        if (!isNullOrEmpty(lastName.get())) {
            lastNameError.set("Please enter last name")
        }

        if (!isNullOrEmpty(emailId.get())) {
            emailIdError.set("Please enter Email Id")
        } else {
            if (isValidEmail(emailId.get())) {
                emailIdError.set("Please enter valid Email Id")
            }
        }
        if (!isNullOrEmpty(emergencyNumber.get())) {
            emergencyNumberError.set("Please enter Emergency number")
        } else {
            if (emergencyNumber.get()?.length!! < 10) {
                emergencyNumberError.set("Emergency number must be atleast 10 numbers")
            }
        }
        if (!isNullOrEmpty(password.get())) {
            passwordError.set("Please enter password")
        } else {
            if (password.get()?.length!! < 6) {
                passwordError.set("Password must be atleast 6 characters")
            }
        }
        if (!isNullOrEmpty(confirmPassword.get())) {
            confirmPasswordError.set("Please enter confirm password")
        } else {
            if (password.get() != confirmPassword.get()) {
                confirmPasswordError.set("Password not matched")
            }
        }
        if (isNullOrEmpty(firstName.get()) &&
            isNullOrEmpty(lastName.get()) &&
            isNullOrEmpty(emailId.get()) &&
            isNullOrEmpty(emergencyNumber.get()) &&
            isNullOrEmpty(password.get()) &&
            !isValidEmail(emailId.get()) &&
            isNullOrEmpty(confirmPassword.get()) &&
            (password.get() == confirmPassword.get())
            && password.get()?.length!! > 5
            && emergencyNumber.get()?.length!! > 9
        ) {
            register()
        }

    }

    private fun clearError() {
        firstNameError.set("")
        lastNameError.set("")
        emailIdError.set("")
        emergencyNumberError.set("")
        passwordError.set("")
        confirmPasswordError.set("")
    }

    private fun register() {
        if (NetworkUtils.isNetWorkAvailable(context)) {
            viewModelScope.launch(Dispatchers.IO) {
                _apiResponse.postValue(Resource(Status.LOADING, null, null))

                kotlin.runCatching {
                    authRepository.register(emailId.get()!!, password.get()!!)
                }.onSuccess { success ->
                    success.collect {
                        _apiResponse.postValue(it)
                    }
                }.onFailure { failure ->
                    print(failure.message)
                }
            }

        } else {
            showWarningMessage(context, "Check your internet connection")
        }

    }


    fun addUserCollection(user: User) {

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {

                _apiResponseUser.postValue(Resource(Status.LOADING, null, null))

                kotlin.runCatching {
                    authRepository.addUserCollection(user)
                }.onSuccess { success ->
                    success.collect {
                        _apiResponseUser.postValue(it)
                    }
                }.onFailure { failure ->
                    print(failure.message)
                }

            }
        }

    }


}