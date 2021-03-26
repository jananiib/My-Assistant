package com.bot.alvinbot.ui.login

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
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    private val _apiResponse = MutableLiveData<Resource<AuthResult>>()
    val apiResponse: MutableLiveData<Resource<AuthResult>> = _apiResponse

    private val _apiResponseUser = MutableLiveData<Resource<DocumentSnapshot>>()
    val apiResponseUser: MutableLiveData<Resource<DocumentSnapshot>> = _apiResponseUser

    var emailId = ObservableField("")
    var password = ObservableField("")


    var emailIdError = ObservableField("")
    var passwordError = ObservableField("")

    fun validation() {
        clearError()



        if (!isNullOrEmpty(emailId.get())) {
            emailIdError.set("Please enter Email Id")
        } else {
            if (isValidEmail(emailId.get())) {
                emailIdError.set("Please enter valid Email Id")
            }
        }

        if (!isNullOrEmpty(password.get())) {
            passwordError.set("Please enter password")
        } else {
            if (password.get()?.length!! < 6) {
                passwordError.set("Password must be atleast 6 characters")
            }
        }


        if (
            isNullOrEmpty(emailId.get()) &&
            isNullOrEmpty(password.get()) && !isValidEmail(emailId.get())
            && password.get()?.length!! > 5
        ) {

            login()
        }

    }

    private fun clearError() {

        emailIdError.set("")
        passwordError.set("")

    }

    private fun login() {

        if (NetworkUtils.isNetWorkAvailable(context)) {
            viewModelScope.launch(Dispatchers.IO) {
                _apiResponse.postValue(Resource(Status.LOADING, null, null))

                kotlin.runCatching {
                    authRepository.login(emailId.get()!!, password.get()!!)
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

    fun getUserCollection(userId: String) {

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {

                _apiResponseUser.postValue(Resource(Status.LOADING, null, null))

                kotlin.runCatching {
                    authRepository.getUserCollection(userId)
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