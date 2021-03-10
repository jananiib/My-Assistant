package com.bot.alvinbot.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bot.alvinbot.R
import com.bot.alvinbot.ui.base.BaseActivity
import com.bot.alvinbot.ui.dashBoard.DashBoardActivity
import com.bot.alvinbot.ui.login.LoginActivity
import com.bot.alvinbot.utils.AskPermission
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {


    @Inject
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (AskPermission.checkVersion()) {
            if (!AskPermission.checkPermission(
                    this,
                    arrayOf(
                        AskPermission.READ_STORAGE,
                        AskPermission.WRITE_STORAGE,
                        AskPermission.LOCATION_COARSE,
                        AskPermission.LOCATION_FINE,
                        AskPermission.READ_CONTACTS,
                        AskPermission.CALL_PHONE,
                        AskPermission.READ_SMS
                    )
                )
            ) {
                AskPermission.requestPermission(
                    this, arrayOf(
                        AskPermission.READ_STORAGE,
                        AskPermission.WRITE_STORAGE,
                        AskPermission.LOCATION_COARSE,
                        AskPermission.LOCATION_FINE,
                        AskPermission.READ_CONTACTS,
                        AskPermission.CALL_PHONE,
                        AskPermission.READ_SMS
                    )
                )
            } else {
                checkLoggedInState()
            }
        }
        setBotImageCornerRadius(findViewById(R.id.iv_logo))


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == AskPermission.PERMISSION_CODE) {
            checkLoggedInState()
        }
    }


    private fun checkLoggedInState() {
        if (auth.currentUser == null) {
            launchScreen(LoginActivity::class.java)
        } else {
            launchScreen(DashBoardActivity::class.java)
        }
    }

    private fun launchScreen(targetActivity: Class<out Any>) {
        lifecycleScope.launch {
            delay(2000L)
            startActivity(Intent(this@SplashScreenActivity, targetActivity))
            finish()
        }
    }
}
