package com.example.testapi

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.await
import androidx.core.content.res.ResourcesCompat
import com.google.android.flexbox.FlexboxLayout
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import kotlinx.coroutines.delay
import java.util.LinkedList

class MainActivity2 : AppCompatActivity() {

    private val repository = YourRepository()

    private lateinit var numAnswerstv: TextView
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var favoriteButton: ImageButton
    private lateinit var repostButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private var a: AnswerSubmission? = null
    private var fa: FavoriteRequest? = null
    private var re: RepostRequest? = null
    private var fo: ForwardRequest? = null
    private lateinit var progressBar: ProgressBar

    private val userId = 123
    private val categoryId = 12

    private val maxPerCategory = 3
    private var questionList: LinkedList<QuestionRowData> = LinkedList()
    private var currentQuestionIndex = -1

    private lateinit var questionId: String
    private lateinit var questionContainer: ConstraintLayout
    private lateinit var answerContainer: FlexboxLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViews()

        lifecycleScope.launch {
            val count = repository.getRecentAnswerCount(userId, categoryId) ; updateProgressBar(count)
            if(count < maxPerCategory) readQuestionRow()
        }

        nextButton.isEnabled = false
        nextButton.setOnClickListener {
            if(questionList.size == maxPerCategory && currentQuestionIndex == questionList.size - 1) {
                lifecycleScope.launch { a?.let { it -> repository.submitAnswer(it) ; readQuestionRow() }} // submit answer
            }

            else if(currentQuestionIndex < questionList.size - 1) {
                lifecycleScope.launch { a?.let { it -> repository.submitAnswer(it) }} // submit answer
                currentQuestionIndex++ // Move forward in existing list
                Log.i("currentQuestionIndex", "curr index: $currentQuestionIndex size: ${questionList.size -1}")
                fillLocalQuestion(questionList[currentQuestionIndex])
            }

            else {
                lifecycleScope.launch {
                    a?.let { it -> repository.submitAnswer(it) } // submit answer
                    readQuestionRow() // load next question
                }
            }
        }

        prevButton.setOnClickListener {
            if (questionList.isEmpty()) {
                Log.i("Message", "No previous questions available.")
            }

            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                Log.i("currentQuestionIndex", "curr index: $currentQuestionIndex size: ${questionList.size -1}")
                lifecycleScope.launch { a?.let { it1 -> repository.submitAnswer(it1) }} // submit answer
                fillLocalQuestion(questionList[currentQuestionIndex])
            } else {
                Log.i("Message", "This is the first question.")
            }
        }

        favoriteButton.setOnClickListener {
            lifecycleScope.launch { fa?.let { it -> repository.addToFavorites(it) }} // submit answer
        }

        repostButton.setOnClickListener {
            lifecycleScope.launch { re?.let { it -> repository.addToReposts(it) }} // submit answer
        }

        forwardButton.setOnClickListener {
            lifecycleScope.launch { fo?.let { it -> repository.addToForwards(it) }} // submit answer
        }

    }

    private fun fillLocalQuestion(questionList: QuestionRowData) {
        fillQuestion(questionList.ID.toString(), questionList.Question.toString(), questionList.answerOptions.toString())
    }

    private fun findViews() {
        numAnswerstv = findViewById(R.id.tv_num_answers)
        nextButton = findViewById(R.id.ib_next_question)
        prevButton = findViewById(R.id.ib_prev_question)
        favoriteButton = findViewById(R.id.ib_favorite)
        repostButton = findViewById(R.id.ib_repost)
        forwardButton = findViewById(R.id.ib_forward)
        progressBar = findViewById(R.id.pb_answers_left)
        questionContainer = findViewById(R.id.cl_question_container)
        answerContainer = findViewById(R.id.fb_answer_container)
    }

    private suspend fun readQuestionRow() {
        try {
            // get number of questions answered for the category
            val count = repository.getRecentAnswerCount(userId, categoryId)

            // update progress bar
            updateProgressBar(count)

            if(count < maxPerCategory) {
                val response = RetrofitClient.apiService.getQuestionRow(categoryId, userId).await()  // ✅ Await the response

                // add all data to linked list
                val questionRowData = QuestionRowData(
                    ID = response.ID.toString(),
                    Category = response.Category.toString(),
                    Question = response.Question.toString(),
                    Format = response.Format.toString(),
                    numberOfAnswers = response.numberOfAnswers,
                    answerOptions = response.answerOptions.toString(),
                    matchingWeight = response.matchingWeight,
                    personalityType = response.personalityType.toString(),
                    reverseScoring = response.reverseScoring.toString()
                )

                questionId = questionRowData.ID.toString()

                questionList.add(questionRowData)
                currentQuestionIndex = questionList.size - 1
                Log.i("currentQuestionIndex", "curr index: $currentQuestionIndex size: ${questionList.size -1}")
                Log.i("Count and Max", "count: $count max: $maxPerCategory")
                Log.i("New Question Read", "ID: $questionId, Question: ${questionRowData.Question}, Category: ${questionRowData.Category}, Format: ${questionRowData.Format}, Number of Answers: ${questionRowData.numberOfAnswers}, Answer Options: ${questionRowData.answerOptions}, Matching Weight: ${questionRowData.matchingWeight}, Personality Type: ${questionRowData.personalityType}, Reverse Scoring: ${questionRowData.reverseScoring}")


                fillQuestion(questionId, questionList[currentQuestionIndex].Question, questionList[currentQuestionIndex].answerOptions)
            }

        } catch (e: Exception) {
            Log.e("API_ERROR", "Failed to fetch data: ${e.message}")
        }
    }

    private fun fillQuestion(questionId: String?, questionText: String?, questionAnsOptions: String?) {
        val customFont = ResourcesCompat.getFont(this, R.font.roboto_semi_bold)

        fa = FavoriteRequest(
            user_id = userId,
            question_id = questionId.toString()
        )

        re = RepostRequest(
            user_id = userId,
            question_id = questionId.toString()
        )

        fo = ForwardRequest(
            sender_id = userId,
            receiver_id = userId,
            question_id = questionId.toString()
        )

        // empty the page
        questionContainer.removeAllViews()
        answerContainer.removeAllViews()
        a = null

        // set the question
        // Create TextView
        val question = TextView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16) // Optional margin
                setPadding(0, 40, 0, 80)
            }
            text = questionText.toString().replaceFirstChar { it.titlecase() } + "?"
            setTextColor(Color.parseColor("#9BBBFF"))
            textSize = 18f
            gravity = Gravity.CENTER
            typeface = customFont
            setLineSpacing(7f, 1.2f)
            id = View.generateViewId() // Generate unique ID for constraints
        }

        questionContainer.apply {
            // Add views to ConstraintLayout
            addView(question)
            setBackgroundResource(R.drawable.question_background)
        }

        // prepare the options
        var answers = parseAnswers(questionAnsOptions)

        val selectedButtons = mutableSetOf<Button>() // Store selected buttons

        for (answer in answers) {
            val button = Button(this).apply {
                text = answer.trim()
                isAllCaps = false
                textSize = 15f
                setTextColor(Color.BLACK)
                gravity = Gravity.CENTER // This centers the text both vertically and horizontally
                typeface = customFont
                elevation = 0f
                setBackgroundResource(R.drawable.answer_background_default)

                // Set padding programmatically
                val horizontalPadding = resources.getDimensionPixelSize(R.dimen.button_padding_horizontal)
                val verticalPadding = resources.getDimensionPixelSize(R.dimen.button_padding_vertical)
                setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

                val pixels = (42 * Resources.getSystem().displayMetrics.density).toInt()

                layoutParams = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    pixels
                ).apply {
                    setMargins(0, 0, 10, 19) // Margin between buttons
                }

                // Remove default minimum height and width
                minimumHeight = 0
                minimumWidth = 0

                setOnClickListener {
                    if (selectedButtons.contains(this)) {
                        // If already selected, deselect it
                        setBackgroundResource(R.drawable.answer_background_default)
                        selectedButtons.remove(this)
                    } else {
                        // Select the button
                        setBackgroundResource(R.drawable.answer_background_clicked)
                        selectedButtons.add(this)
                    }

                    if(selectedButtons.isNotEmpty()) nextButton.isEnabled = true else nextButton.isEnabled = false

                    // Log the selected answers
                    val selectedAnswers = selectedButtons.map { it.text.toString() }

                    a = AnswerSubmission(
                        user_id = userId,
                        question_id = questionId.toString(),
                        selected_answer = selectedAnswers.joinToString("->"),
                        category_id = categoryId
                    )
                    Log.i("AnswerSubmission", "User ID: ${a!!.user_id}, Question ID: ${a!!.question_id}, Category ID: ${a!!.category_id}")
                    Log.i("AnswerSubmission", "Selected Answers: $selectedAnswers")
                }
            }
            answerContainer.addView(button)
        }

    }

    private fun updateProgressBar(count: Int) {
        progressBar.max = 800
        var currProgress = 0

        //currProgress = (count / maxPerCategory) * progressBar.max
        if(count < maxPerCategory) currProgress = count * 333 else currProgress = 1000

        ObjectAnimator.ofInt(progressBar, "progress", currProgress)
            .setDuration(500)
            .start()
    }

    private fun parseAnswers(questionAnsOptions: String?): Array<String> {
        if (questionAnsOptions != null) {
            return questionAnsOptions.split("->").toTypedArray<String>()
        } else {
            return emptyArray<String>()
        }
    }
}