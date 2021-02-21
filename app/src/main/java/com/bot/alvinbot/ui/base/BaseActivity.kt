package com.bot.alvinbot.ui.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bot.alvinbot.R
import com.bot.alvinbot.extensions.isNullOrEmpty

open class BaseActivity : AppCompatActivity() {

    private val CLICK_DRAG_TOLERANCE = 10f
    var downRawX: Float = 0.toFloat()
    var downRawY: Float = 0.toFloat()
    private var dX: Float = 0.toFloat()
    private var dY: Float = 0.toFloat()

    lateinit var progressDialog: Dialog


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
    }

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
        location: String
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

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click

                if (clicked) {

                    Toast.makeText(this, location, Toast.LENGTH_SHORT).show()

                    /*     val smsIntent = Intent(
                             Intent.ACTION_SENDTO,
                             Uri.parse(
                                 "smsto:${sosPrimaryNumber};${sosSecondaryNumber}"
                             )
                         )
                         smsIntent.putExtra(
                             "sms_body",
                             "Hi, This is ${name} (${mobileNumber}). I am taking a trip on a Taxi license plate $plateNumber and need urgent assistance. Here is my live link:\n $link"
                         )
                         startActivity(smsIntent)*/
                }else {
                    //showFailureCustomToast("Please share the link")

                }

                return false
            } else { // A drag
                return true; // Consumed
            }


        } else {
            return super.onTouchEvent(motionEvent)
        }

    }

    fun getImage(imageName: String?): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

}