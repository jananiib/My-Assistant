package com.bot.alvinbot.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivityCameraBinding
import com.bot.alvinbot.ui.base.BaseActivity
import com.camerakit.CameraKit
import com.camerakit.CameraKitView
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.SQLException


@AndroidEntryPoint
class CameraActivity : BaseActivity(), View.OnClickListener {

    private var cameraKitView: CameraKitView? = null
    private val cameraViewModel by viewModels<CameraViewModel>()
    lateinit var binding: ActivityCameraBinding

    companion object {
        const val PICK_IMAGE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_camera)
        binding.lifecycleOwner = this
        binding.cameraViewModel = cameraViewModel
        binding.listener = this

        cameraKitView = binding.camera

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.fbPhoto -> {
                cameraKitView?.captureImage { view, photo ->
                    Thread() {
                        var bitmap: Bitmap? = null
                        try {


                            val options = BitmapFactory.Options()
                            bitmap = BitmapFactory.decodeByteArray(
                                photo,
                                0,
                                photo.size,
                                options
                            )

                            if (bitmap != null) {
                                val textRecognizer = TextRecognizer.Builder(
                                    applicationContext
                                ).build()

                                val imageFrame: Frame = Frame.Builder()
                                    .setBitmap(bitmap)
                                    .build()

                                var imageText = ""

                                val textBlocks = textRecognizer.detect(imageFrame)

                                for (i in 0 until textBlocks.size()) {
                                    val textBlock = textBlocks[textBlocks.keyAt(i)]
                                    imageText = textBlock.value
                                }
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(
                                        this@CameraActivity,
                                        imageText,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                println("bitmap is null")
                            }
                        } catch (e: SQLException) {
                        }


                        /* val jpeg = Jpeg(photo)
                         binding.ib.post { binding.ib.setJpeg(jpeg) }
 */

                    }.start()
                }
            }
            binding.ivFlash -> {
                if (cameraKitView?.flash != CameraKit.FLASH_ON) {
                    cameraKitView?.flash = CameraKit.FLASH_ON
                } else {
                    cameraKitView?.flash = CameraKit.FLASH_OFF
                }
            }
            binding.ivGallery -> {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, PICK_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            var imageUri = data?.data
            //imageView.setImageURI(imageUri)
        }
    }


    override fun onStart() {
        super.onStart()
        cameraKitView?.onStart();
    }

    override fun onStop() {
        super.onStop()
        cameraKitView?.onStop()
    }

    override fun onResume() {
        super.onResume()
        cameraKitView?.onResume()
    }

    override fun onPause() {
        cameraKitView?.onPause()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}