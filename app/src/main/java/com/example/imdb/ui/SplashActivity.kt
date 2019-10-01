package com.example.imdb.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.imdb.R
import com.example.imdb.utils.helper.FlowFragment
import com.example.imdb.utils.helper.observeOnce

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this)
            .get(SplashViewModel::class.java)

        viewModel.apply {
            isSuccessfulInitialization.observeOnce(this@SplashActivity, Observer {
                onSuccessfulInitialization()
            })

            initializeAll()
        }
    }

    private fun onSuccessfulInitialization() {
        // Start application.
        val intent = Intent(this@SplashActivity, ApplicationActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
