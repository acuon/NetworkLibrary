package com.acuon.simplenetworklibrary

import android.net.Uri
import android.util.Log
import com.acuon.simplenetworklibrary.enums.ContentType
import com.acuon.simplenetworklibrary.enums.RequestType
import com.acuon.simplenetworklibrary.listeners.JSONArrayListener
import com.acuon.simplenetworklibrary.listeners.JSONObjectListener
import com.acuon.simplenetworklibrary.listeners.ResponseListener
import com.acuon.simplenetworklibrary.threadExecutor.ThreadExecutor
import com.acuon.simplenetworklibrary.utils.APPLICATION_JSON
import com.acuon.simplenetworklibrary.utils.CONTENT_LENGTH
import com.acuon.simplenetworklibrary.utils.CONTENT_TYPE
import com.acuon.simplenetworklibrary.utils.HttpLogger
import com.acuon.simplenetworklibrary.utils.HttpUtils
import com.acuon.simplenetworklibrary.utils.TEXT_PLAIN
import com.acuon.simplenetworklibrary.utils.UTF_8
import com.acuon.simplenetworklibrary.utils.bufferSize
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object Http {
    internal const val TAG = "Http"

    internal var reqTimeStamp: Long = 0

    interface ProgressCb {
        fun progress(http: Request?, totalRead: Int, totalAvailable: Int, percent: Int)
    }

    class Request {

        internal var requestType: RequestType? = null
        internal var contentType: ContentType? = null
        internal var baseUrl: String? = null
        internal var endpoint: String? = null
        internal val query: MutableMap<String, String> = HashMap()
        internal val header: MutableMap<String, String> = HashMap()
        internal var uri: String? = null
        private var responseListener: ResponseListener? = null
        private var jsonObjReqListener: JSONObjectListener? = null
        private var jsonArrayRequestListener: JSONArrayListener? = null
        private var progressCb: ProgressCb? = null
        internal var body: ByteArray? = null
        internal var loggingEnabled = false
        private var threadExecutor: ThreadExecutor =
            ThreadExecutor().setPriority(ThreadExecutor.DEFAULT)

        fun enableLog(enableLogging: Boolean): Request {
            HttpLogger.isLogsRequired = enableLogging
            loggingEnabled = enableLogging
            return this
        }

        fun setPriority(priority: Int): Request {
            threadExecutor = ThreadExecutor().setPriority(priority)
            return this
        }

        fun url(uri: String?): Request {
            this.uri = uri
            return this
        }

        fun baseUrl(baseUrl: String?): Request {
            this.baseUrl = baseUrl
            return this
        }

        fun endpoint(endpoint: String?): Request {
            this.endpoint = endpoint
            return this
        }

        fun requestType(requestType: RequestType?): Request {
            this.requestType = requestType
            return this
        }

        fun contentType(contentType: ContentType?): Request {
            this.contentType = contentType
            return this
        }

        fun query(queryMap: Map<String, String>?): Request {
            query.putAll(queryMap!!)
            return this
        }

        fun body(json: JSONObject): Request {
            body(json.toString())
            header(CONTENT_TYPE, APPLICATION_JSON)
            return this
        }

        fun body(jsonObjectList: List<JSONObject?>): Request {
            body(jsonObjectList.toString())
            header(CONTENT_TYPE, APPLICATION_JSON)
            return this
        }

        fun body(textBody: String?): Request {
            if (textBody == null) {
                body = null
                return this
            }
            header(CONTENT_TYPE, TEXT_PLAIN)
            try {
                body = textBody.toByteArray(charset(UTF_8))
            } catch (e: UnsupportedEncodingException) { /* Should never happen */
            }
            return this
        }

        fun header(header: Map<String, String>?): Request {
            if (header != null) this.header.putAll(header)
            return this
        }

        fun header(key: String, value: String): Request {
            header[key] = value
            return this
        }

        fun body(rawBody: ByteArray?): Request {
            if (rawBody == null) {
                body = null
                return this
            }
            body = rawBody
            return this
        }

        fun onProgress(progressCb: ProgressCb?) {
            this.progressCb = progressCb
        }

        fun execute(cb: ResponseListener): Request {
            reqTimeStamp = System.currentTimeMillis()
            this.responseListener = cb
            threadExecutor.execute(RequestTask(this))
            return this
        }

        fun execute(cb: JSONObjectListener): Request {
            reqTimeStamp = System.currentTimeMillis()
            this.jsonObjReqListener = cb
            threadExecutor.execute(RequestTask(this))
            return this
        }

        fun execute(cb: JSONArrayListener): Request {
            reqTimeStamp = System.currentTimeMillis()
            this.jsonArrayRequestListener = cb
            threadExecutor.execute(RequestTask(this))
            return this
        }

        internal fun fireProgress(totalRead: Int, totalAvailable: Int) {
            if (progressCb == null) return
            val percent = (totalRead.toFloat() / totalAvailable.toFloat() * 100f).toInt()
            progressCb!!.progress(this@Request, totalRead, totalAvailable, percent)
        }

        fun sendResponse(resp: Response?, e: Exception?) {
            HttpLogger.d(
                TAG,
                "TIME TAKEN FOR API CALL(MILLIS) : " + (System.currentTimeMillis() - reqTimeStamp)
            )
            if (responseListener != null) {
                if (e != null) responseListener!!.onFailure(e)
                else responseListener!!.onResponse(resp?.asString())
                return
            }
            if (jsonObjReqListener != null) {
                if (e != null) jsonObjReqListener!!.onFailure(e)
                else jsonObjReqListener!!.onResponse(resp?.asJSONObject())
                return
            }
            if (jsonArrayRequestListener != null) {
                if (e != null) jsonArrayRequestListener!!.onFailure(e)
                else jsonArrayRequestListener!!.onResponse(resp?.asJSONArray())
                return
            } else e?.printStackTrace()
        }

    }

    internal class RequestTask(private val req: Request) : Runnable {

        private val responseMemoryCache by lazy { ResponseMemoryCache.getInstance() }

        override fun run() {
            try {
//                val cachedResponse: String? = responseMemoryCache.getResponseFromCache(
//                    buildPath(
//                        req.baseUrl,
//                        req.endpoint
//                    ).build().toString()
//                )
//                if (NetworkUtils.stringIsNotEmpty(cachedResponse) && req.responseListener != null) {
//                    req.responseListener?.onResponse(cachedResponse)
//                    Log.d(TAG, "Http : parseResponse : $cachedResponse")
//                } else {
//                    val conn = request()
//                    Log.d(TAG, "Http : parseResponse : ${conn.responseCode}")
//                    parseResponse(conn)
//                }
                val conn = request()
                Log.d(TAG, "Http : parseResponse : ${conn.responseCode}")
                parseResponse(conn)
            } catch (e: IOException) {
                req.sendResponse(null, e)
                Log.d(TAG, "Http : Response is null : ${e.message} ${e.localizedMessage}")
                e.printStackTrace()
            }
        }

        private fun buildPath(baseUrl: String?, endpoint: String?): Uri.Builder {
            var builderPath: Uri.Builder = Uri.Builder()
            if (!baseUrl.isNullOrBlank()) {
                builderPath = Uri.parse(baseUrl).buildUpon()
            } else {
                Log.e(TAG, "No Base URL was set")
            }
            builderPath.appendEncodedPath(endpoint)
            return builderPath
        }

        private fun Uri.Builder.appendQueries(params: Map<String, String>): Uri.Builder {
            for (entry in params.entries) {
                appendQueryParameter(entry.key, entry.value)
            }
            return this
        }

        private fun HttpURLConnection.setContentType(type: ContentType) {
            if (type == ContentType.JSON) {
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
            } else if (type == ContentType.XML) {
                setRequestProperty("Content-Type", "application/xml")
                setRequestProperty("Accept", "application/xml")
            }
        }

        private fun HttpURLConnection.setHeaders(headers: Map<String, String>? = null) {
            if (headers != null) {
                for ((key, value) in headers) {
                    setRequestProperty(key, value)
                }
            }
        }

        @Throws(IOException::class)
        private fun request(): HttpURLConnection {
            val obj = URL(
                buildPath(req.baseUrl, req.endpoint).appendQueries(req.query).build().toString()
            )
            val connection = obj.openConnection() as HttpURLConnection

            when (req.requestType) {
                RequestType.POST, RequestType.PATCH, RequestType.PUT -> {
                    connection.requestMethod = req.requestType?.value
                    connection.setContentType(req.contentType ?: ContentType.JSON)
                    connection.setHeaders(req.header)

                    val outputStream = connection.outputStream
                    outputStream.write(req.body)
                    outputStream.flush()
                    outputStream.close()
                }

                RequestType.GET -> {
                    connection.requestMethod = req.requestType?.value
                    connection.setContentType(req.contentType ?: ContentType.JSON)
                    connection.setHeaders(req.header)
                }

                else -> {}
            }
            if (req.loggingEnabled) {
                HttpLogger.d(TAG, "Http : URL : $obj")
                HttpLogger.d(TAG, "Http : Method : ${req.requestType?.value}")
                HttpLogger.d(TAG, "Http : Headers : " + req.header.toString())
                if (req.body != null) HttpLogger.d(
                    TAG, "Http : Request Body : " + HttpUtils.asString(
                        req.body
                    )
                )
            }
            return connection
        }

        @Throws(IOException::class)
        private fun parseResponse(conn: HttpURLConnection) {
            try {
                val bos = ByteArrayOutputStream()
                val status = conn.responseCode
                if (req.loggingEnabled) HttpLogger.d(
                    TAG,
                    "Http : Response Status Code : " + status + " for URL: " + conn.url
                )
                val message = conn.responseMessage
                val respHeaders =
                    TreeMap<String?, List<String>>(java.lang.String.CASE_INSENSITIVE_ORDER)
                val headerFields: MutableMap<String?, List<String>> = HashMap(conn.headerFields)
                headerFields.remove(null) // null values are not allowed in TreeMap
                respHeaders.putAll(headerFields)
                val validStatus = status in 200..399
                val inpStream = if (validStatus) conn.inputStream else conn.errorStream

                val totalAvailable =
                    if (respHeaders.containsKey(CONTENT_LENGTH)) respHeaders[CONTENT_LENGTH]!![0].toInt() else -1
                if (totalAvailable != -1) req.fireProgress(0, totalAvailable)
                var read: Int
                var totalRead = 0
                val buf = ByteArray(bufferSize)
                while (inpStream.read(buf).also { read = it } != -1) {
                    bos.write(buf, 0, read)
                    totalRead += read
                    if (totalAvailable != -1) req.fireProgress(totalRead, totalAvailable)
                }
                if (totalAvailable != -1) req.fireProgress(totalAvailable, totalAvailable)
                val resp = Response(bos.toByteArray(), status, message, respHeaders)
                if (req.loggingEnabled && !validStatus) HttpLogger.d(
                    TAG,
                    "Http : Response Body : " + resp.asString()
                )
                req.sendResponse(resp, null)
            } finally {
                conn.disconnect()
            }
        }
    }

    class Response(
        private val data: ByteArray,
        val status: Int,
        val message: String,
        val respHeaders: Map<String?, List<String>>
    ) {
        @Throws(JSONException::class)
        fun asJSONObject(): JSONObject {
            val str = asString()
            return if (str.isEmpty()) JSONObject() else JSONObject(str)
        }

        @Throws(JSONException::class)
        fun asJSONArray(): JSONArray {
            val str = asString()
            return if (str.isEmpty()) JSONArray() else JSONArray(str)
        }

        fun asString(): String {
            return HttpUtils.asString(data)
        }
    }
}