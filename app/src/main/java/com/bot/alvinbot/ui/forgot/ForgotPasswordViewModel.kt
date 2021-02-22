package com.bot.alvinbot.ui.forgot

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bot.alvinbot.data.network.Resource
import com.bot.alvinbot.data.network.Status
import com.bot.alvinbot.extensions.isNullOrEmpty
import com.bot.alvinbot.extensions.isValidEmail
import com.bot.alvinbot.repo.AuthRepository
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _apiResponse = MutableLiveData<Resource<Void>>()
    val apiResponse: MutableLiveData<Resource<Void>> = _apiResponse

    var emailId = ObservableField("")

    var emailIdError = ObservableField("")


    fun validation() {
        clearError()



        if (!isNullOrEmpty(emailId.get())) {
            emailIdError.set("Please Enter Email Id")
        }else{
            if (isValidEmail(emailId.get())){
                emailIdError.set("Please Enter Valid Email Id")
            }
        }

        if (
            isNullOrEmpty(emailId.get()) && !isValidEmail(emailId.get())
        ) {
            forgotPassword()
        }

    }

    private fun clearError() {

        emailIdError.set("")
    }

    private fun forgotPassword() {

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {

                _apiResponse.postValue(Resource(Status.LOADING, null, null))

                kotlin.runCatching {
                    authRepository.forgotPassword(emailId.get()!!)
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