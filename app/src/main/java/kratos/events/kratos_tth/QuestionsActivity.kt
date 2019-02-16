package kratos.events.kratos_tth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.*


class QuestionsActivity : AppCompatActivity() {
    private val questions = hashMapOf(
            "Start" to Questions("What is the output of the given Code below",
                    "Input: \nKRATOS\n2K19\nIS\n100%\nBEST!\n",
                    "@drawable/first",
                    "KRATOS",
                    "LH12",
                    "KRATOS"),
            "KRATOS" to Questions("What is the minimum total length for a graph given below with minimum   spanning length of 39 units?",
                    "",
                    "@drawable/second",
                    "64",
                    "NOTICE BOARD",
                    "64"),
            "64" to Questions("What is the output of the given program?",
                    "",
                    "@drawable/third",
                    "5",
                    "4RTH FLOOR PILLAR",
                    "5"),
            "5" to Questions("7 stones arranged in the form of tower.  There are two directions: left and right.  Gold stone is at the top of the tower whose value is 20. Red stone is connected to left side of gold stone and it’s value is 16. Violet stone is connected to the right side of green stone whose value is 22, which is connected to the opposite side of red stone. Value of violet is 40. Blue stone is connected to the opposite side of violet stone and it’s value is 21. Yellow stone and orange stone are opposite to each other whose values are 9 and 19 respectively. Yellow stone is connected to the left  side of red stone. Values get added on traversing each stone. What is the value on reaching violet stone and what is the value of orange stone?",
                    "Note: specify the answer in a,b format",
                    null,
                    "82,19",
                    "STEPS 3-4",
                    "82,19"),
            "82,19" to Questions("Consider a restaurant having 201 tables, numbered from 0 to 200. At some time the waiter is at table 100, and there is a queue of table-access requests for table numbers 30, 85, 90, 100, 105, 110, 135 and 145. What is the ideal list of orders with the overall shortest seek time?,",
                    "Note: Specify the answer in a,b,c,d,e,f,g,h format.",
                    null,
                    "100,105,110,90,85,135,145,30",
                    "DESK",
                    "100,105,110,90,85,135,145,30"),
            "100,105,110,90,85,135,145,30" to Questions("A has two friends B and C. B was friends with D and E. C has two friends F and G. D has two friends H and I. A knew B before he came to know C. B knew D before he came to know E. C would’ve known F before he knew G. D knew H before he came to know I. If D was removed out of the friends circle and B had to establish a friendship connection with I, then print the output of the new BST using DFS implementing PostOrder traversal",
                    "Note: In BST  representation, the left node will hold the friend who was known for a longer time and the right node will hold the friend who was known later. Enter the nodes in x,y,z, order.",
                    null,
                    "H,I,E,B,F,G,C,A",
                    "",
                    "H,I,E,B,F,G,C,A")
    )
    private lateinit var timerTv: TextView
    private lateinit var submitAnswerBtn: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var currentQuestion: Questions
    private lateinit var imageQuestion: ImageView
    private lateinit var questionBelowImage: TextView
    private lateinit var questionsAboveImage: TextView
    private lateinit var textAnswer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        sharedPref = this.getSharedPreferences(getString(R.string.shared_preference_file) ,Context.MODE_PRIVATE)
        val currentQuestionIndex = sharedPref.getString(getString(R.string.current_question_index), "Start")
        currentQuestion = questions[currentQuestionIndex!!]!!
        Log.i("QuestionsActivity", "$currentQuestionIndex ${currentQuestion.answer}")

        submitAnswerBtn = findViewById(R.id.submit_answer)
        questionsAboveImage = findViewById(R.id.question_above_image)
        questionBelowImage = findViewById(R.id.question_below_image)
        imageQuestion = findViewById(R.id.image_question)
        textAnswer = findViewById(R.id.text_answer)
        timerTv = findViewById(R.id.countdown_timer)

        submitAnswerBtn.setOnClickListener {
            if (checkValidation()) {
                if (currentQuestion.nextQuestionIndex == "H,I,E,B,F,G,C,A") {
                    with(sharedPref.edit()) {
                        putString(getString(R.string.current_question_index), currentQuestion.nextQuestionIndex)
                        apply()
                    }
                    onComplete()
                } else {
                    val intent = Intent(applicationContext, CodeScanningActivity::class.java)
                    intent.putExtra("nextQuestionLocation", currentQuestion.nextQuestionLocation)
                    intent.putExtra("expectedBarCodeValue", currentQuestion.nextQuestionIndex)
                    startActivity(intent)
                }
                finish()
            } else {
                Toast.makeText(this@QuestionsActivity, "Invalid answer", Toast.LENGTH_LONG).show()
            }
        }
        startTimer()
        renderCurrentQuestion()
    }

    private fun checkValidation(): Boolean {
        return textAnswer.text.toString() == currentQuestion.answer
    }

    private fun renderCurrentQuestion() {
        questionsAboveImage.text = currentQuestion.questionAboveImage
        questionBelowImage.text = currentQuestion.questionBelowImage
        if (currentQuestion.imageSource !== null) {
            val imageSource = resources.getIdentifier(currentQuestion.imageSource, null, packageName)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageQuestion.setImageDrawable(resources.getDrawable(imageSource, applicationContext.theme))
            } else {
                imageQuestion.setImageDrawable(resources.getDrawable(imageSource))
            }
        }
    }

    private fun startTimer() {
        val endingTime = sharedPref.getLong(getString(R.string.ending_time), Calendar.getInstance().timeInMillis)
        val remainingTime = endingTime - Calendar.getInstance().timeInMillis
        Log.i("CodeScanningActivity", "Time shared pref: $remainingTime")
        if (remainingTime > 0) {
            object : CountDownTimer(remainingTime, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    timerTv.text = "${millisUntilFinished / 60000} : ${(millisUntilFinished / 1000) % 60}"
                    Log.i("CodeScanningActivity", "Time remaining: $millisUntilFinished")
                }

                override fun onFinish() {
                    onComplete()
                }
            }.start()
        }
    }

    private fun onComplete() {
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.is_event_ended), true)
            apply()
        }
        Toast.makeText(this@QuestionsActivity, "Event over", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@QuestionsActivity, ResultActivity::class.java))
        finish()
    }
}
