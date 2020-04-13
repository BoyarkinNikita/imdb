package com.example.imdb.ui.preview

import androidx.navigation.fragment.navArgs
import com.example.imdb.R
import com.example.imdb.databinding.FragmentPreviewBinding
import com.example.imdb.ui.base.MvvmFragment

class PreviewFragment : MvvmFragment<FragmentPreviewBinding, PreviewViewModel>(
    R.layout.fragment_preview,
    PreviewViewModel::class.java
) {
    private val args by navArgs<PreviewFragmentArgs>()

    override fun onFirstInitialization() {
        data.imageUri.value = args.imageUri
    }
}