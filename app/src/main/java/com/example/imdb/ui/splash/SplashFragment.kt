package com.example.imdb.ui.splash

import android.os.Bundle
import com.example.imdb.R
import com.example.imdb.databinding.FragmentSplashBinding
import com.example.imdb.ui.base.MvvmFragment
import com.example.imdb.ui.splash.SplashFragmentDirections.Companion.actionSplashToMain

class SplashFragment : MvvmFragment<FragmentSplashBinding, SplashViewModel>(
    R.layout.fragment_splash,
    SplashViewModel::class.java
) {
    override fun onEveryInitialization(savedBundle: Bundle?) {
        data.eventInitialized.observeEvent(viewLifecycleOwner, {
            applicationNavigation?.navigate(actionSplashToMain())
        })
    }

    override fun onFirstInitialization() {
        data.initializeAll()
    }
}
