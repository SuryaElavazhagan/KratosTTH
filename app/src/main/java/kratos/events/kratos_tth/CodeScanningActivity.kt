package kratos.events.kratos_tth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class CodeScanningActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var mScannerView: ZXingScannerView
    private lateinit var mScanner: RelativeLayout
    private lateinit var mResultTV: TextView
    private lateinit var explanationTextView: TextView
    private lateinit var requestPermissionButton: Button
    private val _CAMERAPERMISSIONCODE: Int = 0
    private var isCameraPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_scanning)

        isCameraPermissionGranted = (ContextCompat.checkSelfPermission(this@CodeScanningActivity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        mScannerView = ZXingScannerView(this@CodeScanningActivity)
        mScanner = findViewById(R.id.scanner)
        mResultTV = findViewById(R.id.result)

        checkPermissionForCamera()

        // Getting next question location
        val nextQuestionLocation = intent.getStringExtra("nextQuestionLocation");
        Log.i("CodeScanningActivity", nextQuestionLocation)

        // Setting the obtained location in bold as note to users
        val noteToUser = SpannableStringBuilder()
        noteToUser.append("Go to ")
        val startingIndex = noteToUser.length
        noteToUser.append(nextQuestionLocation);
        noteToUser.setSpan(StyleSpan(android.graphics.Typeface.BOLD), startingIndex, noteToUser.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        noteToUser.append(" and can the QR code, to get next question")
        mResultTV.text = noteToUser
    }

    public override fun onResume() {
        super.onResume()
        // Trigger bar code scanning on resume, only if camera permission granted
        if (isCameraPermissionGranted) {
            mScannerView.setResultHandler(this)
            mScannerView.startCamera()
        }
    }

    public override fun onPause() {
        super.onPause()
        // Stop bar code scanning on pause, only if camera permission granted
        if(isCameraPermissionGranted) {
            mScannerView.stopCamera()
        }
    }

    override fun handleResult(rawResult: Result?) {
        if(rawResult !== null){
            val result = Integer.parseInt(rawResult.text)
            val sharedPref = this.getSharedPreferences(getString(R.string.shared_preference_file), Context.MODE_PRIVATE)

            with(sharedPref.edit()) {
                putInt(getString(R.string.current_question_number), result)
                apply()
            }

            startActivity(Intent(this@CodeScanningActivity, QuestionsActivity::class.java))
            finish()
        }

    }

    private fun checkPermissionForCamera() {
        if (ContextCompat.checkSelfPermission(this@CodeScanningActivity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@CodeScanningActivity,
                        arrayOf(Manifest.permission.CAMERA), _CAMERAPERMISSIONCODE)
        } else {
            mScanner.addView(mScannerView)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            _CAMERAPERMISSIONCODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    isCameraPermissionGranted = true
                    mScanner.addView(mScannerView)
                } else {
                    explanationTextView = TextView(this@CodeScanningActivity)
                    requestPermissionButton = Button(this@CodeScanningActivity)

                    explanationTextView.text = getString(R.string.permission_explaination)
                    requestPermissionButton.text = getString(R.string.permission_request_button)

                    mScanner.addView(explanationTextView)
                    mScanner.addView(requestPermissionButton)
                }
                return
            }
        }
    }
}
