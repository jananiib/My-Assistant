package com.bot.alvinbot.ui.forgot

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bot.alvinbot.R
import com.bot.alvinbot.data.network.Status
import com.bot.alvinbot.databinding.ActivityForgotPasswordBinding
import com.bot.alvinbot.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    private val forgotPasswordViewModel by viewModels<ForgotPasswordViewModel>()
    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.lifecycleOwner = this
        binding.forgotPasswordViewModel = forgotPasswordViewModel
        binding.listener = this

        setBotImageCornerRadius(binding.ivLogo)

        forgotPasswordObserver()
    }

    private fun forgotPasswordObserver() {
        forgotPasswordViewModel.apiResponse.observe(this, Observer { result ->
            result?.status?.let {
                when (it) {
                    Status.SUCCESS -> {
                        dismissProgressBar()
                        showSuccessCustomToast(
                            "Email sent successfully to reset your password"
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
        }
    }
}