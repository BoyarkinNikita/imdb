package com.example.imdb.utils.helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController

abstract class FlowFragment : Fragment() {
    open fun onBackPressed(): Boolean = false

    protected fun navigation(hostId: Int): NavController = activity?.let {
        findNavController(it, hostId)
    } ?: context?.let {
        if (it is FragmentActivity) findNavController(it, hostId) else null
    } ?: throw IllegalArgumentException("Controller not found for this id.")
}