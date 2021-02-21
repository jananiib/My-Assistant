package com.bot.alvinbot.ui.dashBoard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivityDashBoardBinding
import com.bot.alvinbot.databinding.ItemDashboardInstructionBinding
import com.bot.alvinbot.ui.base.BaseActivity
import com.bot.alvinbot.ui.base.IDashboardSOSVisibleOrNot
import com.bot.alvinbot.ui.camera.CameraActivity
import com.bot.alvinbot.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


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

        viewPagerSetUp(
            listOf(
                ModelInstruction("Title1", "Test1"),
                ModelInstruction("Title2", "Test2"),
                ModelInstruction("Title3", "Test3"),
                ModelInstruction("Title4", "Test4"),
                ModelInstruction("Title5", "Test5")
            )
        )


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


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        dispatchTouchEvents(ev)
        return super.dispatchTouchEvent(ev)
    }


    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        return floatingSos(
            view,
            motionEvent,
            true,
            shareLocation()
        )

    }

    override fun sosVisibleOrNot(visibleOrNot: Boolean) {
        this.visibleOrNot = visibleOrNot

        /*  if (visibleOrNot) {
              binding.ivOrangeSos.visibility = View.VISIBLE
              binding.ivGreySos.visibility = View.GONE
          } else {
              binding.ivOrangeSos.visibility = View.GONE
              binding.ivGreySos.visibility = View.VISIBLE
          }*/
    }

    private fun viewPagerSetUp(result: List<ModelInstruction>) {


        val viewPagerAdapter =
            result.let {
                ViewPagerAdapter(this, it, R.layout.item_dashboard_instruction,
                    object :
                        ViewPagerAdapter.PagerItemCallBack<ItemDashboardInstructionBinding, ModelInstruction> {
                        @SuppressLint("NewApi", "SetTextI18n")
                        override fun onBindItem(
                            viewBinding: ItemDashboardInstructionBinding,
                            data: ModelInstruction,
                            position: Int
                        ) {
                            //  viewBinding.tvPostpaidOrPrepaid.text = data.accountType
                            viewBinding.tvTitle.text = data.title
                            viewBinding.tvBody.text = data.body
                            viewBinding.executePendingBindings()

                        }


                    }
                )
            }



        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.pageMargin =
            resources.getDimensionPixelOffset(R.dimen.pager_margin)
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.clipChildren = false
        binding.viewPager.setPageTransformer(
            false,
            CarouselEffectTransformer(this)
        )

    }

    fun shareLocation(): String {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
                10f,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                    }

                    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
                    override fun onProviderEnabled(s: String) {}
                    override fun onProviderDisabled(s: String) {}
                })
            val myLocation: Location? =
                locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            val longitude: Double? = myLocation?.longitude
            val latitude: Double? = myLocation?.latitude
            val currentTime = Calendar.getInstance().time

            return "As of: ${currentTime}, " +
                    "I am at: http://maps.google.com/?q=" +
                    "${latitude},${longitude} Please emergency"
        } else {
            return ""
        }


    }


}