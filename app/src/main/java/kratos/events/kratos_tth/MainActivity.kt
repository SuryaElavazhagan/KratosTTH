package kratos.events.kratos_tth

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var phoneET: EditText
    private lateinit var startBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private var startingTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        emailET = findViewById(R.id.email_et)
        phoneET = findViewById(R.id.phone_no_et)
        startBtn = findViewById(R.id.start_btn)

        mAuth = FirebaseAuth.getInstance()

        startBtn.setOnClickListener {
            val email = emailET.text.toString()
            val phoneNumber = phoneET.text.toString()

            if(isValidEmail(email) && isValidMobile(phoneNumber)) {
                val startingTime = Calendar.getInstance().timeInMillis
                with(sharedPref.edit()) {
                    putLong(getString(R.string.starting_time), startingTime)
                    android.util.Log.i("CodeScanningActivity","$startingTime")
                    apply()
                }
                startActivity(Intent(applicationContext, QuestionsActivity::class.java))
                finish()
/*                mAuth.createUserWithEmailAndPassword(email, phoneNumber)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                UserActivity.initialCommitToFirebase(email, Integer.parseInt(phoneNumber), object: DatabaseCallbacks {
                                    override fun onSuccess() {
                                        startActivity(Intent(applicationContext, QuestionsActivity::class.java))
                                        finish()
                                    }
                                    override fun onFailure() {
                                        Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }*/
            } else {
                Toast.makeText(this@MainActivity, "Invalid input", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches() && phone.length == 10
    }
}
