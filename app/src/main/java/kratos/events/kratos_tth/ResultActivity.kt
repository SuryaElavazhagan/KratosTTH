package kratos.events.kratos_tth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import java.util.*

class ResultActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var userPhoneNumber: TextView
    private lateinit var questionsAnswered: TextView
    private lateinit var timeSpent: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preference_file), Context.MODE_PRIVATE)

        userName = findViewById(R.id.name_of_participant)
        userPhoneNumber = findViewById(R.id.ph_no_of_participant)
        questionsAnswered = findViewById(R.id.questions_answered_by_participant)
        timeSpent = findViewById(R.id.time_spent_by_participant)

        userName.text = sharedPreferences.getString(getString(R.string.user_email), "")
        userPhoneNumber.text = sharedPreferences.getString(getString(R.string.user_mobile), "")
        questionsAnswered.text = getNumberOfQuestionsAnswered().toString()
        timeSpent.text = "${getTimeSpent()} Minutes"
    }

    private fun getNumberOfQuestionsAnswered(): Int {
        return when (sharedPreferences.getString(getString(R.string.current_question_index), "KRATOS")) {
            "KRATOS" -> 1
            "64" -> 2
            "5" -> 3
            "82,19" -> 4
            "100,105,110,90,85,135,145,30" -> 5
            "H,I,E,B,F,G,C,A" -> 6
            else -> 0
        }
    }

    private fun getTimeSpent(): Long {
        val endingTime = Calendar.getInstance().timeInMillis
        val startingTime = sharedPreferences.getLong(getString(R.string.starting_time), 0)
        val timeSpent = sharedPreferences.getLong("timeSpent", -1L)
        if (timeSpent == -1L) {
            with(sharedPreferences.edit()) {
                putLong("timeSpent", ((endingTime - startingTime) / 60000))
                apply()
            }
            return (endingTime - startingTime) / 60000
        }
        return timeSpent
    }
}
