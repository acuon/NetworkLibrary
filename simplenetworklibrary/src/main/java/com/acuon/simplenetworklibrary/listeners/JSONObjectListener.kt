package com.acuon.simplenetworklibrary.listeners

import org.json.JSONObject

interface JSONObjectListener {
    fun onResponse(res: JSONObject?)
    fun onFailure(e: Exception?)
}
