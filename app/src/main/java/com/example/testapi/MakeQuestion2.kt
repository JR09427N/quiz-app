package com.example.testapi

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class MakeQuestion2 : ComponentActivity() {

    private lateinit var llContentContainer: LinearLayout             
    private val minPollOptions: Int = 2
    private var numPollOptions: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.make_question_activity2)
        findViews()

        fillWithPoll()
    }

    private fun findViews() {
        llContentContainer = findViewById(R.id.ll_content_container)
    }

    private fun fillWithPoll() {
        val customFont = ResourcesCompat.getFont(this, R.font.inter_regular)

        var size = (100 * Resources.getSystem().displayMetrics.density).toInt()
        val ctPollContainer = ConstraintLayout(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                size
            )
            id = View.generateViewId()
        }

        size = (35 * Resources.getSystem().displayMetrics.density).toInt()
        val ivProfilePhoto = ImageView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(size, size).apply {
                setPadding(0, 25, 0, 0)
            }
            //setImageResource(R.drawable.user_pfp_example)
            id = View.generateViewId()
        }

        val etPollStatementWidth = (300 * Resources.getSystem().displayMetrics.density).toInt()
        val etPollStatementHeight = (50 * Resources.getSystem().displayMetrics.density).toInt()
        val etPollStatement = EditText(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                etPollStatementWidth,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(12, 0, 12, 0)
            }
            background = null
            hint = "Poll Statement..."
            setHintTextColor(Color.parseColor("#717171"))
            setTextColor(Color.parseColor("#000000"))
            textSize = 18f
            typeface = customFont
            id = View.generateViewId()
        }

        val ibSuggest = ImageButton(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 20, 0, 0)
            }
            setImageResource(R.drawable.dice_icon)
            background = null
            backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            id = View.generateViewId()
        }

        ctPollContainer.addView(ivProfilePhoto)
        ctPollContainer.addView(etPollStatement)
        ctPollContainer.addView(ibSuggest)

        val set = ConstraintSet()
        set.clone(ctPollContainer)

        set.connect(ivProfilePhoto.id, ConstraintSet.START, ctPollContainer.id, ConstraintSet.START, 0)
        set.connect(ivProfilePhoto.id, ConstraintSet.TOP, ctPollContainer.id, ConstraintSet.TOP, 0)

        set.connect(etPollStatement.id, ConstraintSet.START, ivProfilePhoto.id, ConstraintSet.END, 12)
        set.connect(etPollStatement.id, ConstraintSet.TOP, ctPollContainer.id, ConstraintSet.TOP, 0)

        set.connect(ibSuggest.id, ConstraintSet.END, ctPollContainer.id, ConstraintSet.END, 0)
        set.connect(ibSuggest.id, ConstraintSet.TOP, ctPollContainer.id, ConstraintSet.TOP, 0)

        set.applyTo(ctPollContainer)

        llContentContainer.addView(ctPollContainer)

        while(numPollOptions < 2) {
            addPollOption()
        }


    }

    private fun addPollOption() {
        numPollOptions++
        val customFont = ResourcesCompat.getFont(this, R.font.inter_regular)

        val size = (45 * Resources.getSystem().displayMetrics.density).toInt()
        val etPollStatement = EditText(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                size
            ).apply {
                setPadding(100, 0, 100, 0)
                setMargins(0, 0, 0, 25)
            }
            background = ContextCompat.getDrawable(context, R.drawable.poll_edit_text_background)
            hint = "Option $numPollOptions..."
            setHintTextColor(Color.parseColor("#A1A1A1"))
            setTextColor(Color.parseColor("#000000"))
            textSize = 16f
            typeface = customFont
            id = View.generateViewId()
        }
        llContentContainer.addView(etPollStatement)
    }
}