package com.acuon.simplenetworklibrary.listeners

interface ResponseListener {
    fun onResponse(string: String?)
    fun onFailure(e: Exception?)
}