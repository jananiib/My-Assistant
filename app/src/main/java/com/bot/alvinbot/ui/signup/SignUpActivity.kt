package com.bot.alvinbot.ui.signup

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivitySignUpBinding
import com.bot.alvinbot.extensions.hideView
import com.bot.alvinbot.extensions.showView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private val signUpViewModel by viewModels<SignUpViewModel>()
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.lifecycleOwner = this
        binding.signUpViewModel = signUpViewModel
        binding.listener = this


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
