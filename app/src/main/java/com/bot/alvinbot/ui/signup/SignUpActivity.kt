package com.bot.alvinbot.ui.signup

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bot.alvinbot.R
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
    }

    private fun registerObserver() {
        signUpViewModel.apiResponse.observe(this, Observer { result ->
            result?.status?.let {
                when (it) {
                    Status.SUCCESS -> {
                        hideProgress()
                        auth.signOut()
                        showToast(
                            "Registration Successfully"
                        )
                        finish()
                    }
                    Status.ERROR -> {
                        hideProgress()
                        showToast(
                            result.message.toString()
                        )
                    }
                    Status.LOADING -> {
                        showProgress()
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
