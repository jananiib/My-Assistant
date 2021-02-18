package com.bot.alvinbot.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bot.alvinbot.data.network.Resource
import com.bot.alvinbot.data.network.Status
import com.bot.alvinbot.extensions.isNullOrEmpty
import com.bot.alvinbot.repo.AuthRepository
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _apiResponse = MutableLiveData<Resource<AuthResult>>()
    val apiResponse: MutableLiveData<Resource<AuthResult>> = _apiResponse

    var name = ObservableField("")
    var emailId = ObservableField("")
    var emergencyNumber = ObservableField("")
    var password = ObservableField("")
    var confirmPassword = ObservableField("")
    var maleOfFemale = ObservableField(true)

    var nameError = ObservableField("")
    var emailIdError = ObservableField("")
    var emergencyNumberError = ObservableField("")
    var passwordError = ObservableField("")
    var confirmPasswordError = ObservableField("")


    fun validation() {
        clearError()

        if (!isNullOrEmpty(name.get())) {
            nameError.set("Please Enter Name")
        }

        if (!isNullOrEmpty(emailId.get())) {
            emailIdError.set("Please Enter Email Id")
        }
        if (!isNullOrEmpty(emergencyNumber.get())) {
            emergencyNumberError.set("Please Enter Emergency Number")
        }
        if (!isNullOrEmpty(password.get())) {
            passwordError.set("Please Enter Password")
        }
        if (!isNullOrEmpty(confirmPassword.get())) {
            confirmPasswordError.set("Please Enter Confirm Password")
        }
        if (password.get() != confirmPassword.get()) {
            confirmPasswordError.set("Password not matched")
        }
        if (isNullOrEmpty(name.get()) &&
            isNullOrEmpty(emailId.get()) &&
            isNullOrEmpty(emergencyNumber.get()) &&
            isNullOrEmpty(password.get()) &&
            isNullOrEmpty(confirmPassword.get()) &&
            (password.get() == confirmPassword.get())
        ) {

            // register()

        }

    }

    private fun clearError() {
        nameError.set("")
        emailIdError.set("")
        emergencyNumberError.set("")
        passwordError.set("")
        confirmPasswordError.set("")
    }

    private fun register() {

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {

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
        }

    }


}