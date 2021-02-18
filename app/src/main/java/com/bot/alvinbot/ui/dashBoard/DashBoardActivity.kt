package com.bot.alvinbot.ui.dashBoard

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivityDashBoardBinding
import com.bot.alvinbot.ui.base.BaseActivity
import com.bot.alvinbot.ui.base.IDashboardSOSVisibleOrNot
import com.bot.alvinbot.ui.camera.CameraActivity
import com.bot.alvinbot.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashBoardActivity : BaseActivity(), View.OnClickListener, View.OnTouchListener,
    IDashboardSOSVisibleOrNot {

    private val dashBoardViewModel by viewModels<DashBoardViewModel>()
    lateinit var binding: ActivityDashBoardBinding
    var visibleOrNot = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        binding.lifecycleOwner = this
        binding.dashBoardViewModel = dashBoardViewModel
        binding.listener = this

        binding.ivSos.setOnTouchListener(this)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(50))
        Glide.with(this).load(getImage("logo_bot")).apply(requestOptions)
            .into(binding.ivLogo)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.cvChat -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            binding.cvScan -> {
                startActivity(Intent(this, CameraActivity::class.java))
            }
        }
    }

    fun getImage(imageName: String?): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        dispatchTouchEvents(ev)
        return super.dispatchTouchEvent(ev)
    }


    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return floatingSos(
            view,
            motionEvent,
            "",
            "",
            "",
            "",
            "",
            false,
            ""
        )

    }

    override fun sosVisibleOrNot(visibleOrNot: Boolean) {
        this.visibleOrNot = visibleOrNot

        /*  if (visibleOrNot) {
              dashBoardBinding.ivOrangeSos.visibility = View.VISIBLE
              dashBoardBinding.ivGreySos.visibility = View.GONE
          } else {
              dashBoardBinding.ivOrangeSos.visibility = View.GONE
              dashBoardBinding.ivGreySos.visibility = View.VISIBLE
          }*/
    }


}
