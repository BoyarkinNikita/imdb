package com.example.imdb.ui.main

import android.os.Bundle
import com.example.imdb.R
import com.example.imdb.databinding.FragmentMainBinding
import com.example.imdb.ui.base.ApplicationActivity
import com.example.imdb.ui.base.MvvmFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : MvvmFragment<FragmentMainBinding, MainViewModel>(
    R.layout.fragment_main,
    MainViewModel::class.java
) {
    override fun onEveryInitialization(savedBundle: Bundle?) {
        activity?.setSupportActionBar(mainToolbar)
    }
}