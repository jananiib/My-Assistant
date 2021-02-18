package com.bot.alvinbot.ui.main


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bot.alvinbot.R
import com.bot.alvinbot.ui.adapter.ChatMessageAdapter
import com.bot.alvinbot.data.model.ChatMessage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.alicebot.ab.*
import org.alicebot.ab.Timer
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    // ActionBar actionBar;
    private var mListView: ListView? = null
    private var mButtonSend: FloatingActionButton? = null
    private var mEditTextMessage: EditText? = null
    private var mImageView: ImageView? = null
    var bot: Bot? = null
    private var mAdapter: ChatMessageAdapter? = null
    private var textToSpeech: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val PREFERENCE_NAME = "SharedPreferenceExample"
        val preference = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString(PREFERENCE_NAME, "my name is alvin")




/*
        actionBar=getSupportActionBar();
        ImageButton imageButton=(ImageButton)findViewById(R.id.image___Button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int w = size.x;
                    int h = size.y;

                    Rational aspect = new Rational(w, h);
                    PictureInPictureParams.Builder mPip = new PictureInPictureParams.Builder();
                    mPip.setAspectRatio(aspect).build();

                        enterPictureInPictureMode(mPip.build());
                    }

                }
        });

*/mListView = findViewById<View>(R.id.listView) as ListView
        mButtonSend = findViewById<View>(R.id.btn_send) as FloatingActionButton
        mEditTextMessage = findViewById<View>(R.id.et_message) as EditText
        mImageView = findViewById<View>(R.id.iv_image) as ImageView
        mAdapter = ChatMessageAdapter(this, ArrayList())
        mListView!!.adapter = mAdapter
        mButtonSend!!.setOnClickListener(View.OnClickListener {
            val message = mEditTextMessage!!.text.toString()
            val arr: Array<String>
            val comp: String
            if (message.toUpperCase().startsWith("CALL")) {
                // MAKE CALL
                arr = message.split(" ").toTypedArray()
                makeCall(arr[1])
            } else if (message.toUpperCase().startsWith("OPEN")) {
                //open app
                arr = message.split(" ").toTypedArray()
                comp = getAppName(arr[1])
                launchApp(comp)
            }


            //bot
            val response = chat!!.multisentenceRespond(
                mEditTextMessage!!.text.toString()
            )
            if (TextUtils.isEmpty(message)) {
                return@OnClickListener
            }
            sendMessage(message)
            mimicOtherMessage(response)
            speack(response)
            mEditTextMessage!!.setText("")
            mListView!!.setSelection(mAdapter!!.count - 1)
        })
        //checking SD card availablility
        val a = isSDCARDAvailable
        //receiving the assets from the app directory
        val assets = resources.assets
        val jayDir = File(
            Environment.getExternalStorageDirectory().toString() + "/hari/bots/Alvin"
        )
        val b = jayDir.mkdirs()
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (dir in assets.list("Alvin")!!) {
                    val subdir = File(jayDir.path + "/" + dir)
                    val subdir_check = subdir.mkdirs()
                    for (file in assets.list("Alvin/$dir")!!) {
                        val f =
                            File(jayDir.path + "/" + dir + "/" + file)
                        if (f.exists()) {
                            continue
                        }
                        var `in`: InputStream? = null
                        var out: OutputStream? = null
                        `in` = assets.open("Alvin/$dir/$file")
                        out = FileOutputStream(jayDir.path + "/" + dir + "/" + file)
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(`in`, out)
                        `in`.close()
                        `in` = null
                        out.flush()
                        out.close()
                        out = null
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //get the working directory
        MagicStrings.root_path =
            Environment.getExternalStorageDirectory().toString() + "/hari"
        println("Working Directory = " + MagicStrings.root_path)
        AIMLProcessor.extension = PCAIMLProcessorExtension()
        //Assign the AIML files to bot for processing
        bot = Bot("Alvin", MagicStrings.root_path, "chat")
        chat = Chat(bot)
        val args: Array<String?>? = null
        mainFunction(args)
        initializeTexttoSpeach()

        val list=listOf("hi","my name is alvin","bye")
        list.forEach {
            sendMessage(it)
            val response = chat!!.multisentenceRespond(
                it
            )
            mimicOtherMessage(response)
        }



        //speack(response)
        mEditTextMessage!!.setText("")
        mListView!!.setSelection(mAdapter!!.count - 1)
    }

    /** @Override
     *
     * public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig){
     * if(isInPictureInPictureMode){
     * actionBar.hide();;
     *
     * }else {
     * actionBar.show();
     * }
     * }
     */
    fun getSpeechInput(view: View?) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 10)
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            10 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val result =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                mEditTextMessage!!.setText(result!![0])
            }
        }
    }

    private fun initializeTexttoSpeach() {
        textToSpeech = TextToSpeech(this, OnInitListener {
            if (textToSpeech!!.engines.size == 0) {
                Toast.makeText(this@MainActivity, "pls enter", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                textToSpeech!!.language = Locale.ENGLISH
                val d = "Lets Chat"
                speack(d)
            }
        })
    }

    private fun speack(s: String) {
        if (Build.VERSION.SDK_INT >= 21) {
            textToSpeech!!.speak(s, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            textToSpeech!!.speak(s, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    override fun onPause() {
        super.onPause()
        textToSpeech!!.shutdown()
    }

    private fun sendMessage(message: String) {
        val chatMessage = ChatMessage(message, true, false)
        mAdapter!!.add(chatMessage)


        //mimicOtherMessage(message);
    }

    private fun mimicOtherMessage(message: String) {
        val chatMessage = ChatMessage(message, false, false)
        mAdapter!!.add(chatMessage)
    }

    private fun sendMessage() {
        val chatMessage = ChatMessage(null, true, true)
        mAdapter!!.add(chatMessage)
        mimicOtherMessage()
    }

    private fun mimicOtherMessage() {
        val chatMessage = ChatMessage(null, false, true)
        mAdapter!!.add(chatMessage)
    }

    //copying the file
    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }

    private fun makeCall(name: String) {
        val number = getNumber(name, this@MainActivity)
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number")
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.CALL_PHONE
                    ), 0x12345
                )
            }
        }
        try {
            startActivity(callIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Please grant the permission to call first.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun getNumber(name: String?, context: Context): String {
        val number = ""
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val people =
            context.contentResolver.query(uri, projection, null, null, null)
        val indexName =
            people!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        people.moveToFirst()
        do {
            val Name = people.getString(indexName)
            val Number = people.getString(indexNumber)
            if (Name.equals(name, ignoreCase = true)) {
                return Number.replace("-", "")
            }
            // Do work...
        } while (people.moveToNext())
        return if (!number.equals("", ignoreCase = true)) {
            number.replace("-", "")
        } else number
    }

    fun getAppName(name: String): String {
        val pm = packageManager
        val l =
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
        var packName = ""
        for (ai in l) {
            val n = pm.getApplicationLabel(ai) as String
            if (n.contains(name) || name.contains(n)) {
                packName = ai.packageName
            }
        }
        if (packName === "") packName = "com.nothing"
        return packName
    }

    protected fun launchApp(packageName: String) {
        val mIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (packageName === "com.nothing") {
            Toast.makeText(
                applicationContext,
                "App not found.", Toast.LENGTH_SHORT
            ).show()
        } else if (mIntent != null) {
            try {
                startActivity(mIntent)
            } catch (err: Exception) {
                Toast.makeText(
                    applicationContext,
                    "App not found.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        var chat: Chat? = null

        //check SD card availability
        val isSDCARDAvailable: Boolean
            get() = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) true else false

        //Request and response of user and the bot
        fun mainFunction(args: Array<String?>?) {
            MagicBooleans.trace_mode = false
            println("trace mode = " + MagicBooleans.trace_mode)
            Graphmaster.enableShortCuts = true
            val timer = Timer()
            val request = "Hello."
            val response = chat!!.multisentenceRespond(request)
            println("Human: $request")
            println("Robot: $response")
        }
    }
}

