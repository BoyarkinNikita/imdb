package com.example.imdb.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.imdb.R

class ApplicationActivity : AppCompatActivity() {
    lateinit var sharedData: ApplicationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)

        sharedData = ViewModelProvider(this)
            .get(ApplicationViewModel::class.java)
    }

    override fun onBackPressed() {
        val navigation = supportFragmentManager.findFragmentById(R.id.applicationContainer)
        if (navigation !is NavHostFragment) {
            super.onBackPressed()
            return
        }

        val current = navigation.childFragmentManager.fragments.firstOrNull()
        if (current !is FlowFragment || !current.onBackPressParent()) super.onBackPressed()
    }
}
