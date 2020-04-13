package com.example.imdb.ui.base

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.imdb.R
import kotlin.reflect.KProperty

abstract class FlowFragment : Fragment() {
    protected val activity by ActivityDelegate
    protected val actionBar by ActionBarDelegate

    protected val applicationNavigation by NavigationDelegate(R.id.applicationContainer)
    protected val mainNavigation by NavigationDelegate(R.id.mainContainer)
    protected val bottomNavigation by NavigationDelegate(R.id.bottomContainer)

    open fun onBackPressParent(): Boolean {
        val current = childFragmentManager.fragments.lastOrNull()
        return if (current !is FlowFragment || !current.onBackPressParent()) {
            onBackPressed()
        } else true
    }

    protected open fun onBackPressed(): Boolean = false

    protected fun hideKeyboard() = activity?.run {
        val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    }

    protected fun showKeyboard(view: View) = activity?.run {
        val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private object ActivityDelegate {
        operator fun getValue(
            thisRef: FlowFragment,
            property: KProperty<*>
        ): ApplicationActivity? = with(thisRef) {
            try {
                val activity = (getActivity() ?: context) as? ApplicationActivity?
                if (activity?.isDestroyed == true) null else activity
            } catch (throwable: Throwable) {
                null
            }
        }
    }

    private object ActionBarDelegate {
        operator fun getValue(
            thisRef: FlowFragment,
            property: KProperty<*>
        ): ActionBar? = thisRef.activity?.supportActionBar
    }

    private class NavigationDelegate(
        @IdRes private val hostId : Int
    ) {
        operator fun getValue(
            thisRef: FlowFragment,
            property: KProperty<*>
        ): NavController? = with(thisRef) {
            activity?.let { findNavController(it, hostId) }
        }
    }
}