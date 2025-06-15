package com.example.testapi

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Feed : ComponentActivity() {

    private lateinit var llHeader: LinearLayout
    private lateinit var bnvFooter: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_activity)
        findViews()

    }

    private fun findViews() {
        llHeader = findViewById(R.id.ll_header)
        bnvFooter = findViewById(R.id.bnv_footer_container)
    }
}