package com.app.indianic.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.app.indianic.utils.AppUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    val binding: VB?
        get() = _binding as VB

    private val mProgressDialog: Dialog? by lazy { AppUtil.progressBarLoader(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null)
            throw IllegalArgumentException("Binding cannot be null")
        return binding?.root
    }

    fun <T> collectLatestLifecycleFlow(
        flow: Flow<T>,
        repeatModeOn: Boolean = false,
        collect: suspend (T) -> Unit
    ) {
        if (AppUtil.isInternetAvailable(requireActivity())) {
            viewLifecycleOwner.lifecycleScope.launch {
                if (repeatModeOn) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        flow.collectLatest(collect)
                    }
                } else {
                    flow.collectLatest(collect)
                }
            }
        } else {
        }

    }

    fun showProgress() {
        mProgressDialog?.show()
    }

    fun hideProgress() {
        mProgressDialog?.dismiss()
    }

}