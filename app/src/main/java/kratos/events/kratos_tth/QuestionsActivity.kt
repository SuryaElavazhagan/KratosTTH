package kratos.events.kratos_tth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.os.CountDownTimer



class QuestionsActivity : AppCompatActivity() {
    private val normalPathQuestions: Array<Questions> = arrayOf(
            Questions("What is your name",
                    QuestionType.TEXT,
                    AnswerType.RADIO,
                    "Surya",
                    null,
                    "MB4FLH19",
                    arrayOf("Surya", "Vaibhav", "Sunil", "Siddharth"),
                    0,
                    false),
            Questions("What are the fields you are interested in the below",
                    QuestionType.TEXT,
                    AnswerType.CHECKBOX,
                    null,
                     arrayOf("Riding", "Coding", "GRE", "Singapore"),
                    "MB3FLH21",
                     arrayOf("Riding", "Coding", "GRE", "Singapore"),
                    2,
                    false),
            Questions("Where do you stay?",
                    QuestionType.TEXT,
                    AnswerType.TEXT,
                    "Edho Oru Ooru",
                    null,
                    "",
                    null,
                    1,
                    false)
    )
    private lateinit var submitAnswerBtn: Button
    private lateinit var questionsArea: LinearLayout
    private var isValidAnswerProvided: Boolean = true
    private lateinit var sharedPref: SharedPreferences
    private lateinit var currentQuestion: Questions
    private lateinit var textAnswer: TextView
    private var checkBoxs: Array<CheckBox> = arrayOf()
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        sharedPref = this.getSharedPreferences(getString(R.string.shared_preference_file) ,Context.MODE_PRIVATE)
        val currentQuestionNumber = sharedPref.getInt(getString(R.string.current_question_number), 1)
        Log.i("Questions Activity", "The result is $currentQuestionNumber")

        currentQuestion = normalPathQuestions[currentQuestionNumber]
        submitAnswerBtn = findViewById(R.id.submit_answer)
        questionsArea = findViewById(R.id.questions_area)

        submitAnswerBtn.setOnClickListener {
            if(isValidAnswerProvided) {
                val intent = Intent(applicationContext, CodeScanningActivity::class.java)
                intent.putExtra("nextQuestionLocation", currentQuestion.nextQuestionLocation)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@QuestionsActivity, "Submit a valid answer", Toast.LENGTH_LONG).show()
            }
        }
        renderCurrentQuestion()
    }

    private fun renderCurrentQuestion() {
        if(currentQuestion.isWrongPathQuestion) {
            val mCountDownField = TextView(this)
            object : CountDownTimer(300000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    mCountDownField.text = "${millisUntilFinished / (1000 * 60)} : ${(millisUntilFinished / (1000 * 60 * 60)) % 60}"
                }

                override fun onFinish() {
                    mCountDownField.text = "done!"
                }
            }.start()
        }
        if(currentQuestion.questionType === QuestionType.TEXT) {
            val questionToBeDisplayed = TextView(this)
            questionToBeDisplayed.text = currentQuestion.question
            questionsArea.addView(questionToBeDisplayed)
        }
        when(currentQuestion.answerType) {
            AnswerType.TEXT -> {
                textAnswer = EditText(this)
                textAnswer.hint = "Your answer here"
                questionsArea.addView(textAnswer)
            }
            AnswerType.CHECKBOX -> {
                currentQuestion.options?.forEach {
                    val checkBox = CheckBox(this@QuestionsActivity)
                    checkBox.isChecked = false
                    checkBox.text = it

                    questionsArea.addView(checkBox)
                    checkBoxs += checkBox
                }
            }
            AnswerType.RADIO -> {
                radioGroup = RadioGroup(this@QuestionsActivity)
                radioGroup.orientation = RadioGroup.VERTICAL
                currentQuestion.options?.forEach {
                    val radioButton = RadioButton(this@QuestionsActivity)
                    radioButton.text = it
                    radioGroup.addView(radioButton)
                }
                questionsArea.addView(radioGroup)
            }
        }
    }
}
