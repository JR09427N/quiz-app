package com.example.testapi

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Categories : ComponentActivity() {

    private val repository = YourRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_activity)

        lifecycleScope.launch { fetchSections() }
    }

    private suspend fun fetchSections() {
        try {
            val sections = repository.fetchSections()
            sections.forEach {
                Log.d("Section", "Sort: ${it.sort}, Title: ${it.title}, Description: ${it.description}")
                fillTitle(it.title)
                fillDescription(it.description)
                it.categories.forEach { category ->
                    Log.d("Category", category)
                    addCategory(category)
                }
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    private fun fillTitle(title: String) {
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
    }
}