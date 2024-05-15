package com.acuon.networklibrary.data.remote

import com.acuon.networklibrary.BuildConfig
import com.acuon.simplenetworklibrary.enums.ContentType
import com.acuon.simplenetworklibrary.enums.RequestType
import com.acuon.simplenetworklibrary.Http
import com.acuon.simplenetworklibrary.listeners.ResponseListener
import com.google.gson.Gson
import org.json.JSONObject

class RequestHandler<T>(
    private val endPoint: String,
    private val requestType: RequestType,
    private val contentType: ContentType,
    private val params: Map<String, String>? = null,
    private val headers: Map<String, String>? = null,
    private val requestBody: JSONObject? = null,
    private val onResponse: (T?) -> Unit,
    private val onError: (String?, Exception?) -> Unit,
    private val clazz: Class<T>,
) {

    private var httpRequest: Http.Request? = null

    init {
        httpRequest = Http.Request()
            .requestType(requestType)
            .baseUrl(BuildConfig.BASE_URL)
            .endpoint(endPoint)
            .contentType(contentType)
            .query(params)
            .header(headers)
            .enableLog(BuildConfig.DEBUG)
    }

    fun execute() {
        httpRequest?.execute(object : ResponseListener {
            override fun onResponse(string: String?) {
                val response = Gson().fromJson(string, clazz)
                onResponse.invoke(response)
            }

            override fun onFailure(e: Exception?) {
                onError.invoke(e?.localizedMessage, e)
            }
        })
    }
}