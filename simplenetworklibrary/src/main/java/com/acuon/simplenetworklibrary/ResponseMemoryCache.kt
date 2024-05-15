package com.acuon.simplenetworklibrary

import android.util.LruCache
import com.acuon.simplenetworklibrary.utils.NetworkUtils

internal class ResponseMemoryCache private constructor() {
    private var responseLruCache: LruCache<String, String>? = null

    init {
        responseMemoryCache()
    }

    private fun responseMemoryCache() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        responseLruCache = LruCache(cacheSize)
    }

    fun addResponseToCache(key: String?, response: String?) {
        if (getResponseFromCache(key) == null) {
            responseLruCache?.put(NetworkUtils.getCacheKey(key), response)
        }
    }

    fun getResponseFromCache(key: String?): String? {
        return responseLruCache?.get(NetworkUtils.getCacheKey(key))
    }

    fun clearResponseCache() {
        responseLruCache?.evictAll()
    }

    fun removeResponse(cacheKey: String?) {
        responseLruCache?.remove(NetworkUtils.getCacheKey(cacheKey))
    }

    companion object {
        private var responseMemoryCache: ResponseMemoryCache? = null

        fun getInstance(): ResponseMemoryCache {
            return if (responseMemoryCache == null) ResponseMemoryCache() else responseMemoryCache!!
        }
    }
}

