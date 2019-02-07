package kratos.events.kratos_tth

class Questions(
        val question: String,
        val questionType: QuestionType,
        val answerType: AnswerType,
        val answer: String?,
        val mutliAnswers: Array<String>?,
        val nextQuestionLocation: String,
        var options: Array<String>?,
        val wrongQuestionIndex: Int,
        val isWrongPathQuestion: Boolean
)

enum class QuestionType {
    TEXT, IMAGE
}

enum class AnswerType {
    TEXT, RADIO, CHECKBOX
}