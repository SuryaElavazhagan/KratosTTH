package kratos.events.kratos_tth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var phoneET: EditText
    private lateinit var startBtn: Button
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = this.getSharedPreferences(getString(R.string.shared_preference_file), Context.MODE_PRIVATE)
        emailET = findViewById(R.id.email_et)
        phoneET = findViewById(R.id.phone_no_et)
        startBtn = findViewById(R.id.start_btn)

        checkEventStatus()

        startBtn.setOnClickListener {
            val email = emailET.text.toString()
            val phoneNumber = phoneET.text.toString()

            if (email !== "" && isValidMobile(phoneNumber)) {
                val startingTime = Calendar.getInstance().timeInMillis
                val endingTime = startingTime + 1800000

                Log.i("MainActivity", "$startingTime - $endingTime = ${startingTime - endingTime}")
                with(sharedPref.edit()) {

                    putString(getString(R.string.user_email), email)
                    putString(getString(R.string.user_mobile), phoneNumber)
                    putLong(getString(R.string.starting_time), startingTime)
                    putLong(getString(R.string.ending_time), endingTime)
                    putString(getString(R.string.current_question_index), "Start")
                    putBoolean(getString(R.string.is_event_ended), false)
                    putInt(getString(R.string.no_of_questions_answered), 0)

                    apply()
                }

                Toast.makeText(this@MainActivity, "Event started", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, QuestionsActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@MainActivity, "Invalid input", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidMobile(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches() && phone.length == 10
    }

    private fun checkEventStatus() {
        val isEventEnded = sharedPref.getBoolean(getString(R.string.is_event_ended), false)
        val currentQuestionIndex = sharedPref.getString(getString(R.string.current_question_index), "")
        val endingTime = sharedPref.getLong(getString(R.string.ending_time), 0L)
        val remainingTime = endingTime - Calendar.getInstance().timeInMillis
        Log.i("MainActivity", "$isEventEnded : $remainingTime")
        if (endingTime != 0L) {
            if (isEventEnded || remainingTime < 0) {
                startActivity(Intent(this@MainActivity, ResultActivity::class.java))
                finish()
            }
        }

        if (currentQuestionIndex !== "") {
            startActivity(Intent(applicationContext, QuestionsActivity::class.java))
            finish()
        }

        return
    }
}
