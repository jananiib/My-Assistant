package com.bot.alvinbot.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bot.alvinbot.R
import com.bot.alvinbot.data.local.PreferenceManager
import com.bot.alvinbot.data.local.PreferenceManager.Companion.EMERGENCY_NUMBER
import com.bot.alvinbot.data.local.PreferenceManager.Companion.USER_NAME
import com.bot.alvinbot.data.network.Status
import com.bot.alvinbot.databinding.ActivityLoginBinding
import com.bot.alvinbot.ui.base.BaseActivity
import com.bot.alvinbot.ui.dashBoard.DashBoardActivity
import com.bot.alvinbot.ui.forgot.ForgotPasswordActivity
import com.bot.alvinbot.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private val loginViewModel by viewModels<LoginViewModel>()
    lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel
        binding.listener = this

        setBotImageCornerRadius(binding.ivLogo)

        loginObserver()
        userObserver()

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvForgot -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
            binding.tvSignUp -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }
    }


    private fun userObserver() {
        loginViewModel.apiResponseUser.observe(this, Observer { result ->
            result?.status?.let {
                when (it) {
                    Status.SUCCESS -> {
                        //val user = result.data?.toObject(User::class.java)

                        result.data?.data?.let {
                            if (it.isNotEmpty()) {
                                it["emergencyNumber"]?.let { emergencyNumber ->
                                    preferenceManager.setValue(
                                        EMERGENCY_NUMBER,
                                        emergencyNumber.toString()
                                    )

                                }

                                it["firstName"]?.let { firstName ->
                                    preferenceManager.setValue(
                                        USER_NAME,
                                        "$firstName ${it["lastName"]}"
                                    )
                                }


                            }
                        }



                        dismissProgressBar()
                        startActivity(Intent(this, DashBoardActivity::class.java))
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


    private fun loginObserver() {
        loginViewModel.apiResponse.observe(this, Observer { result ->
            result?.status?.let {
                when (it) {
                    Status.SUCCESS -> {
                        result.data?.user?.let { user ->
                            loginViewModel.getUserCollection(user.uid)
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

}
