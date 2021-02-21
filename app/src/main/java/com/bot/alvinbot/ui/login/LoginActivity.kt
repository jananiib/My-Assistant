package com.bot.alvinbot.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivityLoginBinding
import com.bot.alvinbot.extensions.hideView
import com.bot.alvinbot.extensions.showView
import com.bot.alvinbot.ui.base.BaseActivity
import com.bot.alvinbot.ui.camera.CameraActivity
import com.bot.alvinbot.ui.dashBoard.DashBoardActivity
import com.bot.alvinbot.ui.forgot.ForgotPasswordActivity
import com.bot.alvinbot.ui.main.MainActivity
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

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(50))
        Glide.with(this).load(getImage("logo_bot")).apply(requestOptions)
            .into(binding.ivLogo)

        loginViewModel.valid.observe(this, Observer {
            if (it){
                startActivity(Intent(this, DashBoardActivity::class.java))
                finish()
            }
        })

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

    private fun validation() {
        if (binding.tieUsername.text.isNullOrEmpty()) {
            binding.tvUsernameError.text = ("Username cannot be empty")
            binding.tvUsernameError.showView()
        } else {
            binding.tvUsernameError.text = ("")
            binding.tvUsernameError.hideView()
        }
        if (binding.tiePass.text.isNullOrEmpty()) {
            binding.tvPassError.text = ("Password cannot be empty")
            binding.tvPassError.showView()
        } else {
            binding.tvPassError.text = ("")
            binding.tvPassError.hideView()
        }
        if (!binding.tieUsername.text.isNullOrEmpty() && !binding.tiePass.text.isNullOrEmpty()) {
            startActivity(Intent(this, DashBoardActivity::class.java))
        }
    }

}
