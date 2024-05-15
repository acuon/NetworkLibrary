package com.acuon.simplenetworklibrary.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object NetworkUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun getCacheKey(url: String?): String {
        return url?.replace("[.:/,%?&=]".toRegex(), "+")?.replace("[+]+".toRegex(), "+")
            ?: throw RuntimeException("provided URL is null")
    }

    fun stringIsNotEmpty(string: String?): Boolean {
        if (string != null && string != "null") {
            if (string.trim { it <= ' ' } != "") {
                return true
            }
        }
        return false
    }
}

