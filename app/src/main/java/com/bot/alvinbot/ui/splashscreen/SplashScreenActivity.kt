package com.bot.alvinbot.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import com.bot.alvinbot.R
import com.bot.alvinbot.ui.base.BaseActivity
import com.bot.alvinbot.ui.login.LoginActivity
import com.bot.alvinbot.utils.AskPermission
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : BaseActivity() {

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
                launch()
            }
        }

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(50))
        Glide.with(this).load(getImage("logo_bot")).apply(requestOptions)
            .into(findViewById(R.id.iv_logo))

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
