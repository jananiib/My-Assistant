package com.bot.alvinbot.ui.signup

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bot.alvinbot.R
import com.bot.alvinbot.data.model.User
import com.bot.alvinbot.data.network.Status
import com.bot.alvinbot.databinding.ActivitySignUpBinding
import com.bot.alvinbot.extensions.hideView
import com.bot.alvinbot.extensions.showView
import com.bot.alvinbot.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity(), View.OnClickListener {
    private val signUpViewModel by viewModels<SignUpViewModel>()
    lateinit var binding: ActivitySignUpBinding

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.lifecycleOwner = this
        binding.signUpViewModel = signUpViewModel
        binding.listener = this

        registerObserver()
        userObserver()
    }

    private fun registerObserver() {
        signUpViewModel.apiResponse.observe(this, Observer { result ->
            result?.status?.let {
                when (it) {
                    Status.SUCCESS -> {
                        result.data?.user?.let { user ->
                            auth.signOut()
                            signUpViewModel.addUserCollection(
                                User(
                                    user.uid,
                                    signUpViewModel.firstName.get()!!,
                                    signUpViewModel.lastName.get()!!,
                                    signUpViewModel.emailId.get()!!,
                                    "",
                                    signUpViewModel.emergencyNumber.get()!!,
                                    signUpViewModel.maleOfFemale.get()!!,
                                    0
                                )
                            )
                        }

                    }
                    Status.ERROR -> {
                        dismissProgressBar()
                        showFailureCustomToast(
                            result.message.toString()
                        )
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }

                }

            }

        })
    }

    private fun userObserver() {
        signUpViewModel.apiResponseUser.observe(this, Observer { result ->
            result?.status?.let {
                when (it) {
                    Status.SUCCESS -> {
                        auth.signOut()
                        dismissProgressBar()
                        showSuccessCustomToast(
                            "Registration Successfully"
                        )

                        finish()
                    }
                    Status.ERROR -> {
                        dismissProgressBar()
                        showFailureCustomToast(
                            result.message.toString()
                        )
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }

                }

            }

        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvLogin -> {
                finish()
            }
            binding.btnMale -> {
                binding.ivTickMale.showView()
                binding.ivTickFemale.hideView()
                signUpViewModel.maleOfFemale.set(true)
            }
            binding.btnFemale -> {
                binding.ivTickMale.hideView()
                binding.ivTickFemale.showView()
                signUpViewModel.maleOfFemale.set(false)
            }
        }
    }
}
