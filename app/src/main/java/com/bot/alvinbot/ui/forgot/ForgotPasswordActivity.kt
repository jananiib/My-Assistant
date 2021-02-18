package com.bot.alvinbot.ui.forgot

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivityForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private val forgotPasswordViewModel by viewModels<ForgotPasswordViewModel>()
    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.lifecycleOwner = this
        binding.forgotPasswordViewModel = forgotPasswordViewModel
        binding.listener = this
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvLogin -> {
                finish()
            }
        }
    }
}