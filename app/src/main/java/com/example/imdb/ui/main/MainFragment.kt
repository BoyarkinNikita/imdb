package com.example.imdb.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.imdb.R
import com.example.imdb.utils.helper.FlowFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : FlowFragment() {
    private lateinit var applicationNavigation: NavController
    private lateinit var mainNavigation: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        applicationNavigation = navigation(R.id.applicationContainer)
        mainNavigation = navigation(R.id.mainContainer)

        (activity as? AppCompatActivity?)?.apply {
            setSupportActionBar(mainToolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(true)
                setDisplayHomeAsUpEnabled(false)
                setHomeButtonEnabled(false)
            }
        }

        mainBottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main_movies ->
                    mainNavigation.popBackStack(R.id.destinationFragmentMovies, false)

                R.id.menu_main_news ->
                    mainNavigation.navigate(R.id.destinationFragmentNews)
            }

            true
        }
    }

    override fun onBackPressed(): Boolean = when (mainNavigation.currentDestination?.id) {
        R.id.destinationFragmentMovies -> false

        else -> {
            mainBottomNavigation.selectedItemId = R.id.menu_main_movies
            true
        }
    }
}