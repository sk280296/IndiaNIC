package com.app.indianic.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.app.indianic.utils.AppUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : AppCompatActivity(){

    private var _binding: VB? = null
    val binding: VB get() = _binding as VB

    private val mProgressDialog: Dialog by lazy { AppUtil.progressBarLoader(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = bindingInflater.invoke(layoutInflater)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null")
        }
        setContentView(binding.root)
    }

    fun <T> collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {

        if (AppUtil.isInternetAvailable(this)) {
            lifecycleScope.launch {
                flow.collectLatest(collect)
            }.invokeOnCompletion {
                hideProgress()
            }
        } else {

        }

    }

    fun <T> collectActionFlowNullable(flow: Flow<T>, collect: suspend (T) -> Unit) {
        if (AppUtil.isInternetAvailable(this)) {
            lifecycleScope.launch {
                flow.collectLatest(collect)
            }
        } else {
        }
    }

    fun showProgress() {
        mProgressDialog.show()
    }

    fun hideProgress() {
        mProgressDialog.dismiss()
    }


}