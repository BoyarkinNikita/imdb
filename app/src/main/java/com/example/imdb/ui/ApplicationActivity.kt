package com.example.imdb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.imdb.R
import com.example.imdb.utils.helper.FlowFragment

class ApplicationActivity : AppCompatActivity() {
    private lateinit var viewModel: ApplicationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)

        viewModel = ViewModelProviders.of(this)
            .get(ApplicationViewModel::class.java)

        findNavController(R.id.applicationContainer)
    }

    override fun onBackPressed() {
        val navigation = supportFragmentManager.findFragmentById(R.id.applicationContainer)
        if (navigation !is NavHostFragment) {
            super.onBackPressed()
            return
        }

        val current = navigation.childFragmentManager.fragments.firstOrNull()
        if (current !is FlowFragment || !current.onBackPressed()) super.onBackPressed()
    }
}
