package com.example.testapi

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class UserPoll {
    private val repository = YourRepository()

    private lateinit var llPostContainer: LinearLayout
    private lateinit var llPostHeader: LinearLayout
    private lateinit var ivProfilePhoto: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var ivVerified: ImageView
    private lateinit var tvPostOrigin: TextView
    private lateinit var vSpacer: View
    private lateinit var ibReport: ImageButton
    private lateinit var llPostBody: LinearLayout
    private lateinit var llPollTextContainer: LinearLayout
    private lateinit var tvPollText: TextView
    private lateinit var llPollOptionsContainer: LinearLayout
    private lateinit var llPollOptionsContentContainer: LinearLayout
    private lateinit var ivInstructions: ImageView
    private lateinit var tvInstructions: TextView
    private lateinit var llInstructionsContainer: LinearLayout
    private lateinit var o: Button
    private lateinit var llPostFooter: LinearLayout
    private lateinit var ibFavorite: ImageButton
    private lateinit var ibRepost: ImageButton
    private lateinit var ibMessage: ImageButton

    private val userId = 123
    private lateinit var questionId: String
    private lateinit var pollText: String
    private var category: Int = -1
    private lateinit var pollOptions: Array<String>
    private lateinit var timeAgo: String

    private var a: AnswerSubmission? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun displayFeed(c: Context, uqd: UserQuestionData): LinearLayout {

        parseResponse(uqd)

        llPostContainer = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        llPostHeader = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setPadding(18.dpToPx(c), 10.dpToPx(c), 18.dpToPx(c), 10.dpToPx(c))
            }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        ivProfilePhoto = ImageView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                35.dpToPx(c),
                35.dpToPx(c)
            ).apply {
                setMargins(0, 0, 10.dpToPx(c), 0)
            }
            setImageResource(R.drawable.default_profile_photo)
        }

        val username = "Username"
        tvUsername = TextView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface = ResourcesCompat.getFont(c, R.font.inter_bold)
            setTextColor(Color.parseColor("#BACADF"))
            text = username.uppercase()
        }

        ivVerified = ImageView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                11.dpToPx(c),
                11.dpToPx(c)
            ).apply{
                setMargins(10.dpToPx(c), 0, 10.dpToPx(c), 0)
            }
            setImageResource(R.drawable.verification_mark)
        }

        val postOrigin = "Origin"
        tvPostOrigin = TextView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface = ResourcesCompat.getFont(c, R.font.inter_regular)
            setTextColor(Color.parseColor("#7A818A"))
            text = postOrigin
        }

        vSpacer = View(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                0.dpToPx(c),
                0.dpToPx(c),
                1f
            )
        }

        ibReport = ImageButton(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                19.dpToPx(c),
                19.dpToPx(c)
            ).apply{
                setMargins(10.dpToPx(c), 0, 10.dpToPx(c), 0)
            }
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(c, R.color.transparent))
            setImageResource(R.drawable.report_icon)
        }

        llPostHeader.addView(ivProfilePhoto)
        llPostHeader.addView(tvUsername)
        llPostHeader.addView(ivVerified)
        llPostHeader.addView(tvPostOrigin)
        llPostHeader.addView(vSpacer)
        llPostHeader.addView(ibReport)

        llPostBody = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        llPollTextContainer = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER
            val pollTop = ContextCompat.getDrawable(context, R.drawable.poll_top)
            if(pollTop is GradientDrawable) {
                pollTop.setColor(Color.parseColor("#5DA3FF"))
                this.background = pollTop
            }

        }

        tvPollText = TextView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                235.dpToPx(c),
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{setPadding(0, 45.dpToPx(c), 0, 30.dpToPx(c))}
            (layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER
            gravity = Gravity.CENTER
            typeface = ResourcesCompat.getFont(c, R.font.roboto_semi_bold)
            setTextColor(Color.parseColor("#000000"))
            textSize = 18f
            val s = 6 * c.resources.displayMetrics.density
            setLineSpacing(s, 1.0f)
            text = pollText.replaceFirstChar { it.uppercase() }
        }

        llPollTextContainer.addView(tvPollText)

        llPollOptionsContainer = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                setPadding(0, 30.dpToPx(c), 0, 18.dpToPx(c))
            }
            gravity = Gravity.CENTER_VERTICAL
            orientation = LinearLayout.VERTICAL

            val pollBottom = ContextCompat.getDrawable(context, R.drawable.poll_bottom)
            if(pollBottom is GradientDrawable) {
                pollBottom.setColor(Color.parseColor("#BACADF"))
                this.background = pollBottom
            }
        }

        llPollOptionsContentContainer = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        llInstructionsContainer = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(15.dpToPx(c), 0, 15.dpToPx(c), 17.dpToPx(c)) }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        ivInstructions = ImageView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                15.dpToPx(c),
                15.dpToPx(c)
            ).apply{setMargins(0, 0, 6.dpToPx(c), 0)}
            setImageResource(R.drawable.baseline_info_outline_24)
        }

        val instructions = "Choose one option."
        tvInstructions = TextView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface = ResourcesCompat.getFont(c, R.font.roboto_regular)
            text = instructions
        }

        llInstructionsContainer.addView(ivInstructions)
        llInstructionsContainer.addView(tvInstructions)
        llPollOptionsContainer.addView(llInstructionsContainer)

        val selectedButtons = mutableSetOf<Button>()
        for(option in pollOptions) {
            o = Button(c).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply{
                    setPadding(25.dpToPx(c), 10.dpToPx(c), 0, 10.dpToPx(c))
                    setMargins(15.dpToPx(c), 0, 15.dpToPx(c), 17.dpToPx(c))
                }
                setDrawableColor(context, R.drawable.user_poll_option_background, this, "#5DA3FF")
                (layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER_HORIZONTAL
                typeface = ResourcesCompat.getFont(c, R.font.inter_semi_bold)
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                minWidth = 0
                minHeight = 0
                text = option.trim()
                isAllCaps = false
                textSize = 15f
                setTextColor(Color.BLACK)
                elevation = 0f
                setOnClickListener{
                    if (selectedButtons.contains(this)) {
                        // Deselect the button
                        setDrawableColor(context, R.drawable.user_poll_option_background, this, "#5DA3FF")

                        questionId = uqd.question_id
                        val sortedButtons = selectedButtons.toList().sortedBy { it.text.toString() }
                        val selectedAnswers = sortedButtons.map { it.text.toString() }

                        a = AnswerSubmission(
                            user_id = userId,
                            question_id = questionId,
                            selected_answer = selectedAnswers.joinToString("->"),
                            category_id = category
                        )
                        Log.i("AnswerSubmission", "User ID: ${a!!.user_id}, Question ID: ${a!!.question_id}, Category ID: ${a!!.category_id}")
                        Log.i("AnswerSubmission", "Selected Answers: $selectedAnswers")


                        GlobalScope.launch {
                            repository.deleteAnswer(a!!) // delete answer
                        }

                        selectedButtons.remove(this)
                    } else {
                        // Select the button
                        setDrawableColor(context, R.drawable.user_poll_option_background, this, "#BACADF")

                        // Deselct all other buttons
                        for (item in selectedButtons) {
                            setDrawableColor(context, R.drawable.user_poll_option_background, item, "#5DA3FF")
                        }

                        // add answer to list
                        selectedButtons.clear()
                        selectedButtons.add(this)
                        val sortedButtons = selectedButtons.toList().sortedBy { it.text.toString() }
                        val selectedAnswers = sortedButtons.map { it.text.toString() }

                        questionId = uqd.question_id

                        a = AnswerSubmission(
                            user_id = userId,
                            question_id = questionId,
                            selected_answer = selectedAnswers.joinToString("->"),
                            category_id = category
                        )
                        Log.i("AnswerSubmission", "User ID: ${a!!.user_id}, Question ID: ${a!!.question_id}, Category ID: ${a!!.category_id}")
                        Log.i("AnswerSubmission", "Selected Answers: $selectedAnswers")


                        GlobalScope.launch {
                            repository.submitAnswer(a!!) // submit answer
                        }
                    }
                }
            }
            llPollOptionsContainer.addView(o)
        }
        llPollOptionsContentContainer.addView(llPollOptionsContainer)

        llPostBody.addView(llPollTextContainer)
        llPostBody.addView(llPollOptionsContentContainer)

        llPostFooter = LinearLayout(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setPadding(18.dpToPx(c), 18.dpToPx(c), 18.dpToPx(c), 0) }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        ibFavorite = ImageButton(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                20.dpToPx(c),
                20.dpToPx(c)
            ).apply { setMargins(0, 0, 20.dpToPx(c), 0) }
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(c, R.color.transparent))
            setImageResource(R.drawable.favorite_icon)
        }

        ibRepost = ImageButton(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                20.dpToPx(c),
                20.dpToPx(c)
            ).apply { setMargins(0, 0, 20.dpToPx(c), 0) }
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(c, R.color.transparent))
            setImageResource(R.drawable.repost_icon)
        }

        ibMessage = ImageButton(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                20.dpToPx(c),
                20.dpToPx(c)
            )
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(c, R.color.transparent))
            setImageResource(R.drawable.message_icon)
        }


        val tvTimeStamp = TextView(c).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(18.dpToPx(c), 20.dpToPx(c), 0, 30.dpToPx(c))
            }
            typeface = ResourcesCompat.getFont(c, R.font.inter_medium)
            setTextColor(Color.parseColor("#7A818A"))
            textSize = 14f
            text = timeAgo
        }

        llPostFooter.addView(ibFavorite)
        llPostFooter.addView(ibRepost)
        llPostFooter.addView(ibMessage)

        llPostContainer.addView(llPostHeader)
        llPostContainer.addView(llPostBody)
        llPostContainer.addView(llPostFooter)
        llPostContainer.addView(tvTimeStamp)

        return llPostContainer
    }

    private fun setDrawableColor(c: Context, oldDrawable: Int, item: View, color: String) {
        val newDrawable = ContextCompat.getDrawable(c, oldDrawable)
        if (newDrawable is LayerDrawable) {
            val shapeDrawable = newDrawable.getDrawable(1)
            if (shapeDrawable is GradientDrawable) {
                shapeDrawable.setColor(Color.parseColor(color))
            }
        }
        item.background = newDrawable
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseResponse(uqd: UserQuestionData) {
        pollText = uqd.question_text
        pollOptions = uqd.question_options.split(" -> ").toTypedArray()
        category = uqd.category
        timeAgo = getTimeAgo(uqd.created_at)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeAgo(isoTime: String): String {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val postedTime = LocalDateTime.parse(isoTime, formatter)
        val now = LocalDateTime.now(ZoneId.systemDefault())

        val minutes = ChronoUnit.MINUTES.between(postedTime, now)
        val hours = ChronoUnit.HOURS.between(postedTime, now)
        val days = ChronoUnit.DAYS.between(postedTime, now)

        return when {
            minutes < 1 -> "just now"
            minutes < 60 -> "$minutes min ago"
            hours < 24 -> "$hours hrs ago"
            days < 7 -> "${days}d ago"
            else -> postedTime.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
        }
    }

    private fun Int.dpToPx(c: Context): Int = (this * c.resources.displayMetrics.density).toInt()
}

