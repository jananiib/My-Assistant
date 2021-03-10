package com.bot.alvinbot.ui.camera

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bot.alvinbot.R
import com.bot.alvinbot.databinding.ActivityCameraBinding
import com.bot.alvinbot.databinding.BottomShareBinding
import com.bot.alvinbot.ui.base.BaseActivity
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class CameraActivity : BaseActivity(), View.OnClickListener {

    private val cameraViewModel by viewModels<CameraViewModel>()
    lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var flash: Camera
    private var cameraOnOrOff = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_camera)
        binding.lifecycleOwner = this
        binding.cameraViewModel = cameraViewModel
        binding.listener = this

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listener for take photo button


        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        showProgressBar()
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    dismissProgressBar()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                    imageToText(savedUri)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
            /* .also {
                 it.setAnalyzer(cameraExecutor, LuminosityAnalyzer {
                     Log.d(TAG, "Average luminosity: ")
                 })
             }*/

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                flash = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )


            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun imageToText(imageUri: Uri) {
        try {


            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)


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
                    imageText = imageText.plus("${textBlock.value} \n")
                }
                CoroutineScope(Dispatchers.Main).launch {
                    dismissProgressBar()
                    bottomSheetShare(imageText)
                }
            } else {
                dismissProgressBar()
                println("bitmap is null")
            }
        } catch (e: SQLException) {
            dismissProgressBar()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(com.bot.alvinbot.R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val PICK_IMAGE = 1
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivGallery -> {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, PICK_IMAGE)
            }
            binding.ivFlash -> {
                cameraOnOrOff = !cameraOnOrOff
                if (cameraOnOrOff) {
                    if (flash.cameraInfo.hasFlashUnit()) {
                        flash.cameraControl.enableTorch(true)
                    } else {
                        Toast.makeText(baseContext, "no", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (flash.cameraInfo.hasFlashUnit()) {
                        flash.cameraControl.enableTorch(false)
                    } else {
                        Toast.makeText(baseContext, "no", Toast.LENGTH_SHORT).show()
                    }
                }


            }
            binding.fbPhoto -> {
                takePhoto()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {

                data?.data?.let {
                    imageToText(it)
                }

            }
        }
    }

    private fun bottomSheetShare(
        bodyText: String
    ) {
        val dialog = BottomSheetDialog(this)
        val binding = DataBindingUtil.inflate<BottomShareBinding>(
            LayoutInflater.from(this),
            R.layout.bottom_share,
            null,
            false
        )
        dialog.setContentView(binding.root)


        binding.tvBody.text = bodyText

        binding.fbShare.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Share Text"
            )
            intent.putExtra(Intent.EXTRA_TEXT, bodyText)
            startActivity(Intent.createChooser(intent, "Share Text"))

        }

        binding.ivCopy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", bodyText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                this,
                "Text Copied",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
