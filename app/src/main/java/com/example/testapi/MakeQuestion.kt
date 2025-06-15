package com.example.testapi

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnLayout
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MakeQuestion : ComponentActivity() {

    private val repository = YourRepository()

    private lateinit var etPollStatement: EditText
    private lateinit var llContentContainer: LinearLayout
    private lateinit var llHorizontalButtonsContainer: LinearLayout
    private lateinit var ibPostButton: ImageButton
    private lateinit var ibBackButton: ImageButton
    private lateinit var llAddPollOption: LinearLayout
    private var numPollOptions: Int = 0
    private val minPollOptions: Int = 2
    private val maxPollOptions: Int = 5

    private var q: QuestionSubmission? = null
    private val userId = 123
    private lateinit var questionType: String
    private val pollOptionsList = mutableListOf<EditText>()
    private val removeButtonsList = mutableListOf<ImageButton>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_question_activity)
        findViews()

        addQuestionTypes()
        fillWithPoll()

        ibBackButton.setOnClickListener {
            finish()
        }
    }

    private fun addQuestionTypes() {
        val margRight = 12
        val customFont = ResourcesCompat.getFont(this, R.font.inter_bold)

        val pollButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                setMargins(11, 0, margRight, 0)
                setImageResource(R.drawable.poll_icon)
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                id = View.generateViewId()
            }
        }

        val thisOrThatButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                setMargins(0, 0, margRight, 0)
                setImageResource(R.drawable.this_or_that_icon)
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                id = View.generateViewId()
            }
        }

        val orderingButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                setMargins(0, 0, margRight, 0)
                setImageResource(R.drawable.ordering_icon)
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                id = View.generateViewId()
            }
        }

        val haveYouEverButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                setMargins(0, 0, margRight, 0)
                setImageResource(R.drawable.have_you_ever_icon)
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                id = View.generateViewId()
            }
        }

        val gifButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                setMargins(0, 0, margRight, 0)
                setImageResource(R.drawable.gif_icon)
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                id = View.generateViewId()
            }
        }

        val oneWordButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                setMargins(0, 0, margRight, 0)
                setImageResource(R.drawable.one_word_icon)
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                id = View.generateViewId()
            }
        }

        val pollReplacementButton = Button(this).apply {
            text = "Poll"
            isAllCaps = false
            setTextColor(Color.BLACK)
            typeface = customFont
            textSize = 13f
            background = ContextCompat.getDrawable(context, R.drawable.question_type_background)
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(
                55.dpToPx(),
                30.dpToPx()
            ).apply {
                setMargins(22, 0, margRight, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
            id = View.generateViewId()
        }

        val thisOrThatReplacementButton = Button(this).apply {
            text = "This or That"
            isAllCaps = false
            setTextColor(Color.BLACK)
            typeface = customFont
            textSize = 13f
            background = ContextCompat.getDrawable(context, R.drawable.question_type_background)
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(
                101.dpToPx(),
                30.dpToPx()
            ).apply {
                setMargins(0, 0, margRight, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
            id = View.generateViewId()
        }

        val orderingReplacementButton = Button(this).apply {
            text = "Ordering"
            isAllCaps = false
            setTextColor(Color.BLACK)
            typeface = customFont
            textSize = 13f
            background = ContextCompat.getDrawable(context, R.drawable.question_type_background)
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(
                81.dpToPx(),
                30.dpToPx()
            ).apply {
                setMargins(0, 0, margRight, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
            id = View.generateViewId()
        }

        val haveYouEverReplacementButton = Button(this).apply {
            text = "Have you Ever"
            isAllCaps = false
            setTextColor(Color.BLACK)
            typeface = customFont
            textSize = 13f
            background = ContextCompat.getDrawable(context, R.drawable.question_type_background)
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(
                118.dpToPx(),
                30.dpToPx()
            ).apply {
                setMargins(0, 0, margRight, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
            id = View.generateViewId()
        }

        val gifReplacementButton = Button(this).apply {
            text = "Pick the Gif"
            isAllCaps = false
            setTextColor(Color.BLACK)
            typeface = customFont
            textSize = 13f
            background = ContextCompat.getDrawable(context, R.drawable.question_type_background)
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(
                104.dpToPx(),
                30.dpToPx()
            ).apply {
                setMargins(0, 0, margRight, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
            id = View.generateViewId()
        }

        val oneWordReplacementButton = Button(this).apply {
            text = "One Word"
            isAllCaps = false
            setTextColor(Color.BLACK)
            typeface = customFont
            textSize = 13f
            background = ContextCompat.getDrawable(context, R.drawable.question_type_background)
            minWidth = 0
            minHeight = 0
            setPadding(0, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(
                89.dpToPx(),
                30.dpToPx()
            ).apply {
                setMargins(0, 0, margRight, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
            id = View.generateViewId()
        }

        llHorizontalButtonsContainer.addView(pollReplacementButton)
        llHorizontalButtonsContainer.addView(thisOrThatButton)
        llHorizontalButtonsContainer.addView(orderingButton)
        llHorizontalButtonsContainer.addView(haveYouEverButton)
        llHorizontalButtonsContainer.addView(gifButton)
        llHorizontalButtonsContainer.addView(oneWordButton)

        pollButton.setOnClickListener {
            swapViews(llHorizontalButtonsContainer, pollReplacementButton, pollButton)
            swapViews(llHorizontalButtonsContainer, thisOrThatButton, thisOrThatReplacementButton)
            swapViews(llHorizontalButtonsContainer, orderingButton, orderingReplacementButton)
            swapViews(llHorizontalButtonsContainer, haveYouEverButton, haveYouEverReplacementButton)
            swapViews(llHorizontalButtonsContainer, gifButton, gifReplacementButton)
            swapViews(llHorizontalButtonsContainer, oneWordButton, oneWordReplacementButton)
        }

        thisOrThatButton.setOnClickListener {
            swapViews(llHorizontalButtonsContainer, pollButton, pollReplacementButton)
            swapViews(llHorizontalButtonsContainer, thisOrThatReplacementButton, thisOrThatButton)
            swapViews(llHorizontalButtonsContainer, orderingButton, orderingReplacementButton)
            swapViews(llHorizontalButtonsContainer, haveYouEverButton, haveYouEverReplacementButton)
            swapViews(llHorizontalButtonsContainer, gifButton, gifReplacementButton)
            swapViews(llHorizontalButtonsContainer, oneWordButton, oneWordReplacementButton)
        }

        orderingButton.setOnClickListener {
            swapViews(llHorizontalButtonsContainer, pollButton, pollReplacementButton)
            swapViews(llHorizontalButtonsContainer, thisOrThatButton, thisOrThatReplacementButton)
            swapViews(llHorizontalButtonsContainer, orderingReplacementButton, orderingButton)
            swapViews(llHorizontalButtonsContainer, haveYouEverButton, haveYouEverReplacementButton)
            swapViews(llHorizontalButtonsContainer, gifButton, gifReplacementButton)
            swapViews(llHorizontalButtonsContainer, oneWordButton, oneWordReplacementButton)
        }

        haveYouEverButton.setOnClickListener {
            swapViews(llHorizontalButtonsContainer, pollButton, pollReplacementButton)
            swapViews(llHorizontalButtonsContainer, thisOrThatButton, thisOrThatReplacementButton)
            swapViews(llHorizontalButtonsContainer, orderingButton, orderingReplacementButton)
            swapViews(llHorizontalButtonsContainer, haveYouEverReplacementButton, haveYouEverButton)
            swapViews(llHorizontalButtonsContainer, gifButton, gifReplacementButton)
            swapViews(llHorizontalButtonsContainer, oneWordButton, oneWordReplacementButton)
        }

        gifButton.setOnClickListener {
            swapViews(llHorizontalButtonsContainer, pollButton, pollReplacementButton)
            swapViews(llHorizontalButtonsContainer, thisOrThatButton, thisOrThatReplacementButton)
            swapViews(llHorizontalButtonsContainer, orderingButton, orderingReplacementButton)
            swapViews(llHorizontalButtonsContainer, haveYouEverButton, haveYouEverReplacementButton)
            swapViews(llHorizontalButtonsContainer, gifReplacementButton, gifButton)
            swapViews(llHorizontalButtonsContainer, oneWordButton, oneWordReplacementButton)
        }

        oneWordButton.setOnClickListener {
            swapViews(llHorizontalButtonsContainer, pollButton, pollReplacementButton)
            swapViews(llHorizontalButtonsContainer, thisOrThatButton, thisOrThatReplacementButton)
            swapViews(llHorizontalButtonsContainer, orderingButton, orderingReplacementButton)
            swapViews(llHorizontalButtonsContainer, haveYouEverButton, haveYouEverReplacementButton)
            swapViews(llHorizontalButtonsContainer, gifButton, gifReplacementButton)
            swapViews(llHorizontalButtonsContainer, oneWordReplacementButton, oneWordButton)
        }
    }

    private fun swapViews(parent: LinearLayout, view1: View, view2: View) {
        val index = parent.indexOfChild(view2)
        if(index != -1) {
            parent.removeViewAt(index)
            parent.addView(view1, index)
        }
    }

    private fun fillWithPoll() {
        questionType = "poll"

        val interRegular = ResourcesCompat.getFont(this, R.font.inter_regular)

        val llHorizontal = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                setMargins(0, 0, 0, 25.dpToPx())
                orientation = LinearLayout.HORIZONTAL
                id = View.generateViewId()
            }
        }

        val ivProfilePic = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                30.dpToPx(),
                30.dpToPx()
            ).apply {
                setPadding(0)
                setImageResource(R.drawable.default_profile_photo)
                id = View.generateViewId()
            }
        }

        etPollStatement = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                background = null
                setPadding(0)
                setMargins(6.dpToPx(), 4.dpToPx(), 0, 0)
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                hint = "Poll Question..."
                setHintTextColor(Color.parseColor("#598eff"))
                setTextColor(Color.parseColor("#598eff"))
                textSize = 16f
                typeface = interRegular
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
                id = View.generateViewId()
            }
        }

        llHorizontal.addView(ivProfilePic)
        llHorizontal.addView(etPollStatement)
        llContentContainer.addView(llHorizontal)

        etPollStatement.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etPollStatement.doOnLayout{
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etPollStatement, InputMethodManager.SHOW_IMPLICIT)
        }

        llAddPollOption = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                setMargins(0, 6.dpToPx(), 0, 0)
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_HORIZONTAL
                id = View.generateViewId()
                setOnClickListener {
                    addPollOption()
                }
            }
        }

        val tvAddOption = TextView(this).apply{
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                textSize = 15f
                typeface = ResourcesCompat.getFont(context, R.font.inter_bold)
                setTextColor(Color.parseColor("#ffffff"))
                text = "Add option"
                gravity = Gravity.CENTER_VERTICAL
            }
        }

        val ibAddOptionButton = ImageButton(this).apply{
            layoutParams = LinearLayout.LayoutParams(
                20.dpToPx(),
                20.dpToPx()
            ).apply{
                setMargins(5.dpToPx(), 2.dpToPx(), 0, 0)
                background = ContextCompat.getDrawable(context, R.drawable.add_poll_option_circle)
                gravity = Gravity.CENTER_VERTICAL
            }
        }

        llAddPollOption.addView(tvAddOption)
        llAddPollOption.addView(ibAddOptionButton)

        while(numPollOptions < minPollOptions) {
            addPollOption()
        }

        ibPostButton.setOnClickListener {
            // get poll statement
            val pollStatement = etPollStatement.text.toString()

            // get poll options
            val pollOptionsString = pollOptionsList.joinToString(separator = " -> ") { it.text.toString() }

            Log.i("UserID", userId.toString())
            Log.i("QuestionType", questionType)
            Log.i("PollStatement", pollStatement)
            Log.i("PollOptions", pollOptionsString)

            q = QuestionSubmission (
                user_id = userId,
                question_type = questionType,
                question_text = pollStatement,
                question_options = pollOptionsString
            )

            lifecycleScope.launch { q?.let { repository.submitQuestion(it) }} // submit poll question

            val intent = Intent(this, MainActivity3::class.java)
            intent.putExtra("navigateToFragmentId", R.id.feedFragment)
            startActivity(intent)
        }
    }

    private fun addPollOption() {
        val interRegular = ResourcesCompat.getFont(this, R.font.inter_regular)

        val flContainer = FrameLayout(this)
        val numPollOptionsText = numPollOptions + 1

        val etPollOption = EditText(this).apply{
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                40.dpToPx()
            ).apply{
                setPadding(10.dpToPx(), 0, 45.dpToPx(), 0)
                setMargins(35.dpToPx(), 0, 35.dpToPx(), 12.dpToPx())
                background = ContextCompat.getDrawable(context, R.drawable.poll_edit_text_background)
                setHintTextColor(Color.parseColor("#CBDBFF"))
                hint = "Option $numPollOptionsText..."
                setTextColor(Color.parseColor("#ffffff"))
                textSize = 16f
                typeface = interRegular
                id = View.generateViewId()
            }
        }

        val ibRemovePollOption = ImageButton(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                20.dpToPx(),
                20.dpToPx()
            ).apply {
                setImageResource(R.drawable.remove_option_icon)
                background = null
                gravity = Gravity.END or Gravity.CENTER_VERTICAL
                minimumHeight = 0.dpToPx()
                minimumWidth = 0.dpToPx()
                setMargins(0, 0, 45.dpToPx(), 6.dpToPx())
                isEnabled = pollOptionsList.size >= 2
            }

            setOnClickListener {
                pollOptionsList.remove(etPollOption)
                removeButtonsList.remove(this)
                llContentContainer.removeView(flContainer)
                numPollOptions--
                val shouldEnable = pollOptionsList.size >= 2
                removeButtonsList.forEach { it.isEnabled = shouldEnable }
                updateButtonState()
                updateHints()
                if (llAddPollOption.parent == null && numPollOptions < maxPollOptions) llContentContainer.addView(llAddPollOption)
            }
        }

        removeButtonsList.add(ibRemovePollOption)
        if (etPollOption.parent != null) (etPollOption.parent as ViewGroup).removeView(etPollOption)
        flContainer.addView(etPollOption)
        flContainer.addView(ibRemovePollOption)

        if (llAddPollOption.parent == llContentContainer) llContentContainer.removeView(llAddPollOption)
        llContentContainer.addView(flContainer); numPollOptions++

        val indexOfPollOption = llContentContainer.indexOfChild(flContainer)
        Log.i("Poll Option Index", indexOfPollOption.toString())

        if(llAddPollOption.parent == null && numPollOptions < maxPollOptions) llContentContainer.addView(llAddPollOption)

        pollOptionsList.add(etPollOption)

        etPollOption.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateHints() {
        pollOptionsList.forEachIndexed { index, editText ->
            editText.hint = "Option ${index + 1}..."
        }
    }

    fun updateButtonState() {
        val nonEmptyCount = pollOptionsList.count { it.text.toString().trim().isNotEmpty() }
        ibPostButton.isEnabled = nonEmptyCount >= 2 && etPollStatement.text.toString().trim().isNotEmpty()
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun findViews() {
        llContentContainer = findViewById(R.id.ll_content_container)
        llHorizontalButtonsContainer = findViewById(R.id.ll_horizontal_buttons_container)
        ibPostButton = findViewById(R.id.ib_next_button)
        ibPostButton.isEnabled = false
        ibBackButton = findViewById(R.id.ib_back_button)
    }
}