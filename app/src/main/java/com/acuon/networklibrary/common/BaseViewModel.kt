package com.acuon.networklibrary.common

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    private val mIsLoading = ObservableBoolean()

    fun getIsLoading(): ObservableBoolean {
        return mIsLoading
    }

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }

    /**
     * executes a task in coroutine with specified Dispatcher
     *
     * @param dispatcher The coroutine dispatcher to use, Default is Dispatchers.Main
     * @param job The suspend function or task to be executed
     *
     * Usage example:
     * ```
     * execute {
     *     //your task
     * }
     * ```
     */
    fun execute(dispatcher: CoroutineContext = Dispatchers.Main, job: suspend () -> Unit) =
        viewModelScope.launch(dispatcher) {
            job.invoke()
        }


    /**
     * ignores a coroutine exception based on the provided throwable and invokes a callback
     *
     * @param throwable The throwable representing the exception
     * @param callback The callback function to invoke if the exception condition is met
     *
     * Usage example:
     * ```
     * ignoreCoroutineException(exception) {
     *     //handle the execption
     * }
     * ```
     */
    fun ignoreCoroutineException(throwable: Throwable, callback: () -> Unit) {
        if (throwable.message?.contains("Standalone") != true)
            callback.invoke()
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
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(delayMilliSec)
                withContext(Dispatchers.Main) {
                    job.invoke()
                }
            }
        }
}