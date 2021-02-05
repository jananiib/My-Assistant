package com.bot.alvinbot.splashscreen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bot.alvinbot.AskPermission
import com.bot.alvinbot.R
import com.bot.alvinbot.login.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

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
            }

            else{
                launch()
            }
        }


    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == AskPermission.PERMISSION_CODE) {
            launch()
        }
    }


    private fun launch() {
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            finish()
        }

    }
}
