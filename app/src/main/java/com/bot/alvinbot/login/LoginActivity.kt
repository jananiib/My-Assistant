package com.bot.alvinbot.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bot.alvinbot.MainActivity
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivityLoginBinding
import com.bot.alvinbot.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fabLogin.setOnClickListener {
            validation()
        }


        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


    }

    private fun validation() {
        if (binding.tieUsername.text.isNullOrEmpty()) {
            binding.tvUsernameError.text = ("Username cannot be empty")
            binding.tvUsernameError.visibility = View.VISIBLE
        } else {
            binding.tvUsernameError.text = ("")
            binding.tvUsernameError.visibility = View.GONE
        }
        if (binding.tiePass.text.isNullOrEmpty()) {
            binding.tvPassError.text = ("Password cannot be empty")
            binding.tvPassError.visibility = View.VISIBLE
        } else {
            binding.tvPassError.text = ("")
            binding.tvPassError.visibility = View.GONE
        }
        if (!binding.tieUsername.text.isNullOrEmpty() && !binding.tiePass.text.isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
