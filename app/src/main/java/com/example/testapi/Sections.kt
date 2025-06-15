package com.example.testapi

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import coil.decode.SvgDecoder
import coil.request.ImageRequest


class Sections : ComponentActivity() {

    private val repository = YourRepository()

    private lateinit var contentContainer: LinearLayout
    private lateinit var ivCat: ImageView
    private lateinit var ibBackButton: ImageButton
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_activity)
        findViews()

        lifecycleScope.launch { fetchSections() }

        ibBackButton.setOnClickListener { finish() }
    }

    private fun findViews() {
        contentContainer = findViewById(R.id.ll_content_container)
        ibBackButton = findViewById(R.id.ib_back_button)
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
        var size = 0

        val customFont = ResourcesCompat.getFont(this, R.font.roboto_medium)

        size = (67 * Resources.getSystem().displayMetrics.density).toInt()
        val catContainer = LinearLayout(this).apply {
            orientation = HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, size)
            setBackgroundResource(R.drawable.category_background)
            setPadding(40, 50, 20, 0)

            setOnClickListener {
                intent = Intent(this@Sections, MainActivity2::class.java)
                intent.putExtra("CATEGORY_INDEX", getCatId(cat.lowercase().replace(" ", "")))
                startActivity(intent)
            }
        }

        size = (25 * Resources.getSystem().displayMetrics.density).toInt()
        ivCat = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams(size, size)
            )
        }

        val tvCat = TextView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(52, 0, 0, 0) // Optional margin
            }
            text = cat
            setTextColor(Color.parseColor("#ffffff"))
            textSize = 16f
            typeface = customFont
        }

        // Spacer view to push ibCat to the right
        val spacer = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, 0, 1f) // weight = 1
        }

        val sizeArrow = (25 * Resources.getSystem().displayMetrics.density).toInt()
        val ibCat = ImageView(this).apply {
            setBackgroundResource(R.drawable.baseline_keyboard_arrow_left_24)
            layoutParams = LinearLayout.LayoutParams(sizeArrow, sizeArrow).apply {
                setMargins(0, 0, 0, 0)
            }
        }

        catContainer.addView(ivCat)
        catContainer.addView(tvCat)
        catContainer.addView(spacer)
        catContainer.addView(flipImageHorizontally(ibCat))

        contentContainer.addView(catContainer)

        ivCat.context.let {
            coil.ImageLoader(it).enqueue(getCategoryImage(cat))
        }
    }

    private fun getCategoryImage(cat: String): ImageRequest {
        val svgUrl = "http://10.0.2.2:8000/svgs/${cat.lowercase().replace(" ", "")}.svg"

        return ImageRequest.Builder(this)
            .data(svgUrl)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true)
            .target(ivCat)
            .build()
    }

    private fun flipImageHorizontally(imageView: ImageView): ImageView {
        imageView.scaleX *= -1 // Reverse the scale on the X-axis

        return imageView
    }

    private fun getCatId(input: String): Int {
        val i = when (input) {
            "lifestyle" ->  1
            "relationships" -> 2
            "beliefs" -> 3
            "ambition" -> 4
            "communication" -> 5
            "money" -> 6
            "emotionalintelligence" -> 7
            "boundaries" -> 8
            "ethics" -> 9
            "intimacy" -> 10
            "leadership" -> 11
            "attraction" -> 12
            else -> -1
        }
        return i
    }
}