package com.acuon.simplenetworklibrary.utils

import android.util.Log

internal object HttpLogger {
    var isLogsRequired = true
    fun d(tag: String?, message: String?) {
        if (isLogsRequired) Log.d(tag, message!!)
    }

    fun e(tag: String?, message: String?) {
        if (isLogsRequired) Log.e(tag, message!!)
    }
}