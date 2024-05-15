package com.acuon.networklibrary.common

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.acuon.networklibrary.utils.extensions.hideSoftKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivity<T : ViewDataBinding?> :
    AppCompatActivity(), BaseFragment.Callback {

    protected var binding: T? = null


    // layout reference
    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onFragmentAttached() {}
    override fun onFragmentDetached(tag: String) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        setupViews()
        bindViewModel()
    }

    fun getViewDataBinding() = binding!!

    fun hasPermission(permission: String?): Boolean {
        return checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionsSafely(permissions: Array<String?>?, requestCode: Int) {
        requestPermissions(permissions!!, requestCode)
    }

    protected abstract fun setupViews()
    protected abstract fun bindViewModel()
    protected abstract fun onViewClicked(view: View?)

    /**
     * clickListener for views
     */
    var clickListener =
        View.OnClickListener { view ->
            hideSoftKeyboard()
            onViewClicked(view)
        }

    //setting the binding variable
    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding!!.executePendingBindings()
    }

    /**
     * runs a job or task after a delay of set interval time
     *
     * @param delayMilliSec - time for which the task should be delayed
     * @param job - your action or task
     *
     * Usage Example:
     *```runDelayed(1000L) {
     *      //your task
     * }
     */
    fun runDelayed(delayMilliSec: Long, job: suspend () -> Unit) =
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(delayMilliSec)
                withContext(Dispatchers.Main) {
                    job.invoke()
                }
            }
        }

}