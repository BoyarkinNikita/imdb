package com.example.imdb.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.imdb.BR

abstract class MvvmFragment<T : ViewDataBinding, V : BaseViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClazz: Class<V>
) : FlowFragment() {
    protected lateinit var sharedData: ApplicationViewModel

    protected lateinit var binding: T
    protected lateinit var data: V

    protected var isFirstCreation: Boolean = true

    @MainThread
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, layoutRes,
            container, false
        ) ?: return null

        return binding.root
    }

    @MainThread
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        data = ViewModelProvider(this).get(viewModelClazz).apply { onCreate() }

        isFirstCreation = data.isFirstCreation

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.data, data)
        }
    }

    @MainThread
    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = activity ?: return

        sharedData = activity.sharedData
        data.sharedData = activity.sharedData

        binding.setVariable(BR.sharedData, activity.sharedData)

        onEveryInitialization(savedInstanceState)

        if (isFirstCreation) onFirstInitialization()
    }

    @MainThread
    protected open fun onEveryInitialization(savedBundle: Bundle?) {
        // Nothing to do here.
    }

    @MainThread
    protected open fun onFirstInitialization() {
        // Nothing to do here.
    }
}