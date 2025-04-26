package com.example.testapi

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding

class MakeQuestion : ComponentActivity() {

    private lateinit var llContentContainer: LinearLayout
    private lateinit var llHorizontalButtonsContainer: LinearLayout
    private lateinit var ibAddOptionButton: ImageButton
    private var numPollOptions: Int = 0
    private val minPollOptions: Int = 2
    private val maxPollOptions: Int = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_question_activity)
        findViews()

        addQuestionTypes()
        fillWithPoll()
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
        val interRegular = ResourcesCompat.getFont(this, R.font.inter_regular)

        val llHorizontal = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                setMargins(0, 0, 0, 100)
                orientation = LinearLayout.HORIZONTAL
                id = View.generateViewId()
            }
        }

        val ivProfilePic = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setPadding(0)
                setImageResource(R.drawable.default_profile_photo)
                id = View.generateViewId()
            }
        }

        val etPollStatement = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                background = null
                setPadding(0)
                setMargins(11, 0, 0, 0)
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                hint = "Ask a question..."
                setHintTextColor(Color.parseColor("#598eff"))
                setTextColor(Color.parseColor("#ffffff"))
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

        etPollStatement.postDelayed({
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etPollStatement, InputMethodManager.SHOW_IMPLICIT)
        }, 100)

        ibAddOptionButton = ImageButton(this).apply{
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (40 * Resources.getSystem().displayMetrics.density).toInt()
            ).apply{
                background = ContextCompat.getDrawable(context, R.drawable.add_option_button)
                setOnClickListener {
                    addPollOption()
                }
            }
        }

        while(numPollOptions < minPollOptions) {
            addPollOption()
        }
    }

    private fun addPollOption() {
        val interRegular = ResourcesCompat.getFont(this, R.font.inter_regular)
        var numPollOptionsText = numPollOptions + 1

        val etPollOption = EditText(this).apply{
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (40 * Resources.getSystem().displayMetrics.density).toInt()
            ).apply{
                setPadding(50, 0, 50, 0)
                setMargins(0, 0, 0, 25)
                background = ContextCompat.getDrawable(context, R.drawable.poll_edit_text_background)
                hint = "Option $numPollOptionsText..."
                setHintTextColor(Color.parseColor("#598eff"))
                setTextColor(Color.parseColor("#ffffff"))
                textSize = 13f
                typeface = interRegular
                id = View.generateViewId()
            }
        }

        llContentContainer.removeView(ibAddOptionButton)
        llContentContainer.addView(etPollOption)
        if(numPollOptions < maxPollOptions) llContentContainer.addView(ibAddOptionButton)
        numPollOptions++
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun findViews() {
        llContentContainer = findViewById(R.id.ll_content_container)
        llHorizontalButtonsContainer = findViewById(R.id.ll_horizontal_buttons_container)
    }
}