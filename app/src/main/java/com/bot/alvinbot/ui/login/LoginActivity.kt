package com.bot.alvinbot.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bot.alvinbot.R
import com.bot.alvinbot.data.network.Status
import com.bot.alvinbot.databinding.ActivityLoginBinding
import com.bot.alvinbot.ui.base.BaseActivity
import com.bot.alvinbot.ui.dashBoard.DashBoardActivity
import com.bot.alvinbot.ui.forgot.ForgotPasswordActivity
import com.bot.alvinbot.ui.signup.SignUpActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private val loginViewModel by viewModels<LoginViewModel>()
    lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel
        binding.listener = this

        setBotImageCornerRadius(binding.ivLogo)

        loginObserver()

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

    private fun loginObserver() {
        loginViewModel.apiResponse.observe(this, Observer { result ->
            result?.status?.let {
                when (it) {
                    Status.SUCCESS -> {
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

}
