package com.bot.alvinbot.ui.base

import android.content.Context
import android.graphics.Rect
import android.telephony.SmsManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bot.alvinbot.utils.widgets.CProgressDialog
import com.bot.alvinbot.utils.widgets.CustomToast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlin.math.abs

open class BaseActivity : AppCompatActivity(), IBaseView {

    private val CLICK_DRAG_TOLERANCE = 10f
    var downRawX: Float = 0.toFloat()
    var downRawY: Float = 0.toFloat()
    private var dX: Float = 0.toFloat()
    private var dY: Float = 0.toFloat()
/*
    lateinit var progressDialog: Dialog*/

    private var cProgressDialog: CProgressDialog? = null

    override fun showProgressBar() {
        if (!isFinishing) progressDialog().show()
    }

    override fun dismissProgressBar() {
        if (!isFinishing && cProgressDialog != null && cProgressDialog?.isShowing()!!) cProgressDialog?.dismiss()
    }

    private fun progressDialog(): CProgressDialog {
        if (cProgressDialog == null) {
            cProgressDialog = CProgressDialog(this)
        }
        return cProgressDialog!!
    }

/*
    fun showProgress() {
        progressDialog = Dialog(this)

        progressDialog.apply {

            setContentView(R.layout.dialog_progress)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }

    }

    fun hideProgress() {
        progressDialog.apply {
            dismiss()
        }
    }*/

    fun dispatchTouchEvents(ev: MotionEvent?) {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect: Rect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
    }

    fun floatingSos(
        view: View,
        motionEvent: MotionEvent,
        clicked: Boolean,
        location: String,
        mobileNo: String
    ): Boolean {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams

        val action = motionEvent.action
        if (action == MotionEvent.ACTION_DOWN) {

            downRawX = motionEvent.rawX
            downRawY = motionEvent.rawY
            dX = view.x - downRawX
            dY = view.y - downRawY

            return true // Consumed

        } else if (action == MotionEvent.ACTION_MOVE) {

            val viewWidth = view.width
            val viewHeight = view.height

            val viewParent = view.parent as View
            val parentWidth = viewParent.width
            val parentHeight = viewParent.height

            var newX = motionEvent.rawX + dX
            newX = Math.max(
                layoutParams.leftMargin.toFloat(),
                newX
            ) // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(
                (parentWidth - viewWidth - layoutParams.rightMargin).toFloat(),
                newX
            ) // Don't allow the FAB past the right hand side of the parent

            var newY = motionEvent.rawY + dY
            newY = Math.max(
                layoutParams.topMargin.toFloat(),
                newY
            ) // Don't allow the FAB past the top of the parent
            newY = Math.min(
                (parentHeight - viewHeight - layoutParams.bottomMargin).toFloat(),
                newY
            ) // Don't allow the FAB past the bottom of the parent

            view.animate()
                .x(newX)
                .y(newY)
                .setDuration(0)
                .start()

            return true // Consumed

        } else if (action == MotionEvent.ACTION_UP) {

            val upRawX = motionEvent.rawX
            val upRawY = motionEvent.rawY

            val upDX = upRawX - downRawX
            val upDY = upRawY - downRawY

            return if (abs(upDX) < CLICK_DRAG_TOLERANCE && abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                if (clicked) {
                    sendSMS(mobileNo, location)
                }
                false
            } else { // A drag
                true; // Consumed
            }


        } else {
            return super.onTouchEvent(motionEvent)
        }

    }

    fun getImage(imageName: String?): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    fun setBotImageCornerRadius(ivLogo: AppCompatImageView) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(50))
        Glide.with(this).load(getImage("logo_bot")).apply(requestOptions)
            .into(ivLogo)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    fun showSuccessCustomToast(msg: String) {
        CustomToast.makeText(this, msg, 2000, CustomToast.SUCCESS).show()
    }

    fun showFailureCustomToast(msg: String) {
        CustomToast.makeText(this, msg, 2000, CustomToast.ERROR).show()
    }

    fun showWarningCustomToast(msg: String) {
        CustomToast.makeText(this, msg, 2000, CustomToast.WARNING).show()
    }

    fun showInfoCustomToast(msg: String) {
        CustomToast.makeText(this, msg, 2000, CustomToast.INFO).show()
    }

    open fun sendSMS(phoneNo: String?, msg: String?) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
            showSuccessCustomToast("Message Sent Successfully")
        } catch (ex: Exception) {
            showFailureCustomToast(ex.message.toString())
            ex.printStackTrace()
        }
    }


}