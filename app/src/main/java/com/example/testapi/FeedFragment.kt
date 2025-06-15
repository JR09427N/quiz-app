package com.example.testapi

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.testapi.RetrofitClient.apiService
import kotlinx.coroutines.launch

class FeedFragment : Fragment() {

    private lateinit var llHeader: LinearLayout
    private lateinit var llContentContainer: LinearLayout
    private lateinit var llPostContainer: LinearLayout
    private val userPoll = UserPoll()
    private var uqd: UserQuestionData? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        findViews(view)

        displayHeader()
        callDisplayFeed()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callDisplayFeed() {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = apiService.getUserQuestion()
            if (response.isSuccessful) {
                val questions = response.body()
                if(!questions.isNullOrEmpty()) {
                    for(question in questions) {
                        if (question != null && question.is_active) {
                            val uqd = UserQuestionData(
                                question_id = question.question_id,
                                question_text = question.question_text,
                                question_options = question.question_options,
                                question_type = question.question_type,
                                category = question.category,
                                created_at = question.created_at,
                                is_active = question.is_active
                            )
                            Log.d("Question Read", "QuestionID: ${uqd.question_id}, QuestionText: ${uqd.question_text}, QuestionOptions: ${uqd.question_options}, QuestionType: ${uqd.question_type}, CreatedAt: ${uqd.created_at}, IsActive: ${uqd.is_active}")

                            val llPostContainer = userPoll.displayFeed(requireContext(), uqd)
                            llContentContainer.addView(llPostContainer)
                        }
                    }
                } else {
                    Log.e("Question Read", "No active question found.")
                }
            } else {
                Log.e("Question Read", "API call failed: ${response.code()}")
            }
        }
    }


    private fun displayHeader() {
        headerLevel1()
        headerLevel2()
    }

    private fun headerLevel1() {
        lateinit var tvTop: TextView
        lateinit var tvAppName: TextView
        lateinit var tvNew: TextView

        llHeader.setPadding(0, 15.dpToPx(), 0, 0)

        val indicator = View(context).apply {
            setBackgroundColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(0, 2.dpToPx())
        }

        val llHorizontal = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }

        tvTop = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 15.dpToPx(), 5.dpToPx()) }
            textSize = 17f
            alpha = 0.75f
            typeface = ResourcesCompat.getFont(context, R.font.inter_bold)
            setTextColor(Color.parseColor("#ffffff"))
            text = "Top"
            setOnClickListener {
                tvTop.alpha = 1f
                tvAppName.alpha = 0.75f
                tvNew.alpha = 0.75f
                moveIndicatorTo(tvTop, indicator, llHorizontal)
            }
        }

        llHorizontal.post {
            moveIndicatorTo(tvTop, indicator, llHorizontal)
        }

        tvAppName = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 15.dpToPx(), 5.dpToPx()) }
            textSize = 17f
            alpha = 0.75f
            typeface = ResourcesCompat.getFont(context, R.font.inter_bold)
            setTextColor(Color.parseColor("#ffffff"))
            text = "JSRP2"
            setOnClickListener {
                tvTop.alpha = 0.75f
                tvAppName.alpha = 1f
                tvNew.alpha = 0.75f
                moveIndicatorTo(tvAppName, indicator, llHorizontal)
            }
        }

        tvNew = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 5.dpToPx()) }
            textSize = 17f
            alpha = 0.75f
            typeface = ResourcesCompat.getFont(context, R.font.inter_bold)
            setTextColor(Color.parseColor("#ffffff"))
            text = "New"
            setOnClickListener {
                tvTop.alpha = 0.75f
                tvAppName.alpha = 0.75f
                tvNew.alpha = 1f
                moveIndicatorTo(tvNew, indicator, llHorizontal)
            }
        }

        llHorizontal.addView(tvTop)
        llHorizontal.addView(tvAppName)
        llHorizontal.addView(tvNew)
        llHeader.addView(llHorizontal)
        llHeader.addView(indicator)
    }

    private fun moveIndicatorTo(textView: TextView, indicator: View, parent: LinearLayout) {
        val location = IntArray(2)
        textView.getLocationOnScreen(location)
        val parentLocation = IntArray(2)
        parent.getLocationOnScreen(parentLocation)

        val left = location[0] - parentLocation[0]
        val width = textView.width

        indicator.animate()
            .x(left.toFloat() + parent.left)
            .setDuration(200)
            .start()

        val params = indicator.layoutParams
        params.width = width
        indicator.layoutParams = params
    }

    private fun headerLevel2() {
        val llHorizontal = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{
                setMargins(0, 28.dpToPx(), 0, 13.dpToPx())
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }

        val cat = newLL(context, R.drawable.categories_icon, 23.dpToPx(), "categories")
        val fav = newLL(context, R.drawable.favorites_icon, 23.dpToPx(), "favorites")
        val cre = newLL(context, R.drawable.create_icon, 23.dpToPx(), "create")
        val his = newLL(context, R.drawable.history_icon, 23.dpToPx(), "history")
        val res = newLL(context, R.drawable.responses_icon, 31.dpToPx(), "responses")

        llHorizontal.addView(cat)
        llHorizontal.addView(fav)
        llHorizontal.addView(cre)
        llHorizontal.addView(his)
        llHorizontal.addView(res)

        cat.setOnClickListener {
            Log.i("Message", "Redirect to cat")
            val intent = Intent(context, Sections::class.java)
            startActivity(intent)
        }

        llHeader.addView(llHorizontal)
    }

    private fun newLL(context: Context?, image: Int, size: Int, text: String): LinearLayout {
        val ll = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply{setMargins(14.dpToPx(), 0, 14.dpToPx(), 0)}
            orientation = LinearLayout.VERTICAL
        }

        val ib = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                size,
                23.dpToPx()
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                setMargins(0, 0, 0, 10.dpToPx())
            }
            background = context?.let { ContextCompat.getDrawable(it, image) }
        }

        val tv = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER_HORIZONTAL
            textSize = 10f
            typeface = context?.let { ResourcesCompat.getFont(it, R.font.roboto_bold) }
            setTextColor(Color.parseColor("#ffffff"))
        }
        tv.text = text.uppercase()

        ll.addView(ib)
        ll.addView(tv)

        return ll
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun findViews(view: View) {
        llHeader = view.findViewById(R.id.ll_header)
        llContentContainer = view.findViewById(R.id.ll_content_container)
    }
}
