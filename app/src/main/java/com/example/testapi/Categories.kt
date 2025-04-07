package com.example.testapi

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.InputStream

class Categories : ComponentActivity() {

    private val repository = YourRepository()

    private lateinit var contentContainer: LinearLayout
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_activity)
        findViews()

        lifecycleScope.launch { fetchSections() }
    }

    private fun findViews() {
        contentContainer = findViewById(R.id.ll_content_container)
        imageView = findViewById(R.id.imageView)
    }

    private suspend fun fetchSections() {
        try {
            val sections = repository.fetchSections()
            sections.forEach {
                val sort = it.sort
                val title = it.title
                val desc = it.description

                Log.d("Section", "Sort: $sort, Title: $title, Description: $desc")

                addTitle(title)
                addDesc(desc)

                it.categories.forEach { category ->
                    Log.d("Category", category)

                    addCat(category)
                }
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    private fun addTitle(title: String) {
        val customFont = ResourcesCompat.getFont(this, R.font.roboto_regular)

        val title = TextView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20, 56, 0, 16)
            }
            text = title
            setTextColor(Color.parseColor("#BACADF"))
            textSize = 17f
            gravity = Gravity.CENTER
            typeface = customFont
            id = View.generateViewId()
        }

        contentContainer.apply {
            addView(title)
        }
    }

    private fun addDesc(desc: String) {
        val customFont = ResourcesCompat.getFont(this, R.font.roboto_regular)

        val desc = TextView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20, 16, 0, 56)
            }
            text = desc
            setTextColor(Color.parseColor("#D2DCE8"))
            textSize = 14f
            typeface = customFont
            id = View.generateViewId()
        }

        contentContainer.apply {
            addView(desc)
        }
    }

    private fun addCat(cat: String) {
    }
}