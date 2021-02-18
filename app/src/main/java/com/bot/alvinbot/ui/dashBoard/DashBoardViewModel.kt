package com.bot.alvinbot.ui.dashBoard

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
class DashBoardViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _apiResponse = MutableLiveData<Resource<AuthResult>>()
    val apiResponse: MutableLiveData<Resource<AuthResult>> = _apiResponse

    var emailId = ObservableField("")
    var password = ObservableField("")


    var emailIdError = ObservableField("")
    var passwordError = ObservableField("")



    fun validation() {
        clearError()



        if (!isNullOrEmpty(emailId.get())) {
            emailIdError.set("Please Enter Email Id")
        }

        if (!isNullOrEmpty(password.get())) {
            passwordError.set("Please Enter Password")
        }

        if (
            isNullOrEmpty(emailId.get()) &&
            isNullOrEmpty(password.get())
        ) {

            // register()

        }

    }

    private fun clearError() {

        emailIdError.set("")
        passwordError.set("")

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