package com.bot.alvinbot.ui.forgot

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bot.alvinbot.R
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

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(50))
        Glide.with(this).load(getImage("logo_bot")).apply(requestOptions)
            .into(binding.ivLogo)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvLogin -> {
                finish()
            }
        }
    }
}