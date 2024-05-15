package com.acuon.simplenetworklibrary.threadExecutor

import android.os.Handler
import android.os.HandlerThread
import android.os.Process

class ThreadExecutor {
    private val defaultHandlerThread =
        HandlerThread(PRIORITY_HANDLER_THREAD, Process.THREAD_PRIORITY_DEFAULT)
    private val mediumHandlerThread =
        HandlerThread(PRIORITY_HANDLER_THREAD_1, Process.THREAD_PRIORITY_MORE_FAVORABLE)
    private val highHandlerThread =
        HandlerThread(PRIORITY_HANDLER_THREAD_2, Process.THREAD_PRIORITY_FOREGROUND)

    private var priority = DEFAULT
    fun setPriority(priority: Int): ThreadExecutor {
        this.priority = priority
        return this
    }

    private val handlerThread: HandlerThread?
        get() {
            when (priority) {
                DEFAULT -> return defaultHandlerThread
                MEDIUM -> return mediumHandlerThread
                HIGH -> return highHandlerThread
            }
            return null
        }

    fun execute(runnable: Runnable?) {
        val handlerThread = handlerThread ?: return
        val handler = Handler(handlerThread.looper)
        handler.post(runnable!!)
    }

    companion object {
        const val DEFAULT = 0
        const val MEDIUM = -1
        const val HIGH = -2
        private const val PRIORITY_HANDLER_THREAD = "PriorityHandlerThread"
        private const val PRIORITY_HANDLER_THREAD_1 = "PriorityHandlerThread-1"
        private const val PRIORITY_HANDLER_THREAD_2 = "PriorityHandlerThread-2"
    }

    init {
        defaultHandlerThread.start()
        mediumHandlerThread.start()
        highHandlerThread.start()
    }
}