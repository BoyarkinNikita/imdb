package com.example.imdb.ui.main.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.imdb.R
import com.example.imdb.databinding.FragmentMovieDetailsBinding
import com.example.imdb.utils.helper.FlowFragment

class MovieDetailsFragment : FlowFragment() {
    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMovieDetailsBinding>(
            inflater, R.layout.fragment_movie_details,
            container, false
        ) ?: return null

        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)

        viewModel.imdbId = arguments?.getString("imdbId") ?: return null

        return binding.also {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadDetails()
    }
}