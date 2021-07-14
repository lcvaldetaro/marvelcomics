package com.example.comics

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class Network {
    companion object {
        var throttleling = true
        val RETRY_COUNT = 3
        var RETRY_WAIT : Long = 3000
        var MAX_CONCURRENT_CALLS = 8
        var WAIT_QUEUE_TIME: Long = 250
        var CONNECT_TIMEOUT = 1000
        var READ_TIMEOUT = 15000
        var DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36 "
        var AGENT_SUFFIX = ""

        private var current = 0
        private val TAG = Network::class.java.simpleName

        @JvmStatic @Synchronized private fun throttle() {
            if (throttleling) {
                if (++current >= MAX_CONCURRENT_CALLS) {
                    while (current >= MAX_CONCURRENT_CALLS)
                        Thread.sleep(WAIT_QUEUE_TIME)
                }
            }
        }

        @JvmStatic @Synchronized private fun deThrottle () {
            if (throttleling)
                current--
        }

        @JvmStatic fun post(baseUrl: String, postParam: String, contentType: String): HttpResponse {
            return post(baseUrl, "", postParam, contentType, "")
        }

        @JvmStatic fun post(baseUrl: String, qString: String, postParam: String, contentType: String): HttpResponse {
            return post(baseUrl, qString, postParam, contentType, "")
        }

        @JvmStatic fun post(baseUrl: String, qString: String, postParam: String, contentType: String, token: String?): HttpResponse {
            var retry = RETRY_COUNT
            var httpResponse = HttpResponse()
            while (retry-- >= 0) {
                try {
                    httpResponse = restPost(baseUrl, qString, postParam, contentType, token);
                    break
                }
                catch (e: Exception) {
                    Log.e(TAG+"-post", "got exception " + e)
                    if (retry == 0) {
                        httpResponse.responseCode = -1
                        break
                    }
                    Thread.sleep(RETRY_WAIT)
                }
            }
            return httpResponse
        }

        @JvmStatic fun restPost(baseUrl: String, qString: String, postParam: String, pContentType: String, token: String?): HttpResponse {
            val SUBTAG = "-respPost(String)"
            Log.e(TAG+SUBTAG, "Query String: $qString")
            Log.e(TAG+SUBTAG, "postParam: $postParam")

            val mURL = URL("${baseUrl}${qString}")
            var httpResponse = HttpResponse()

            throttle()

            try {
                val httpUrlConn : HttpURLConnection

                httpUrlConn = mURL.openConnection() as HttpURLConnection

                with(httpUrlConn) {
                    requestMethod = "POST"

                    setRequestProperty("Content-Type", pContentType)
                    if (token != "")
                        setRequestProperty("Authorization", "Bearer ${token}")

                    connectTimeout = CONNECT_TIMEOUT
                    readTimeout = READ_TIMEOUT
                    setRequestProperty("User-Agent", DEFAULT_AGENT + AGENT_SUFFIX)

                    if (postParam != "") {
                        val wr = OutputStreamWriter(getOutputStream())
                        wr.write(postParam)
                        wr.flush()
                    }

                    Log.e(TAG+SUBTAG, "URL : $url")
                    Log.e(TAG+SUBTAG, "Response Code : $responseCode")

                    httpResponse.responseCode = responseCode

                    val streamUsed = if (responseCode == 200) inputStream else errorStream

                    if (contentType != null) {
                        if ((contentType ?: "").contains("json") || (contentType ?: "").contains("text")) {
                            BufferedReader(InputStreamReader(streamUsed)).use {
                                val response = StringBuffer()
                                var inputLine = it.readLine()
                                while (inputLine != null) {
                                    response.append(inputLine)
                                    inputLine = it.readLine()
                                }

                                Log.d(TAG+SUBTAG, "Response : '${response}'")

                                httpResponse.response = response.toString()
                            }
                        } else
                            if (contentLength > 0) {
                                var array = ByteArray(contentLength)
                                val dataIs = DataInputStream(streamUsed)
                                dataIs.readFully(array)
                                httpResponse.byteResponse = array
                                httpResponse.response = array.toString(Charsets.UTF_8)
                            } else {
                                val dataIs = DataInputStream(streamUsed)
                                val size = 1024 * 1024
                                val array = ByteArray(size)
                                val r = dataIs.read(array)
                                if (r != -1) {
                                    val newarray = array.copyOf(r)
                                    httpResponse.response = newarray.toString(Charsets.UTF_8)
                                    httpResponse.contentLength = httpResponse.response.length
                                } else
                                    Log.e(TAG+SUBTAG, "No data")
                            }
                    }
                }

                deThrottle()
                return httpResponse;
            }
            catch (e: java.lang.Exception) {
                throw (e)
            }

        }

        @JvmStatic fun post(baseUrl: String, postParam: ByteArray, contentType: String): HttpResponse {
            return post(baseUrl, "", postParam,  contentType, "")
        }

        @JvmStatic fun post(
            baseUrl: String,
            qString: String,
            postParam: ByteArray,
            contentType: String
        ): HttpResponse {
            return post(baseUrl, qString, postParam, contentType,"")
        }

        @JvmStatic fun post(baseUrl: String, qString: String, postParam: ByteArray, contentType: String, token: String?): HttpResponse {
            val SUBTAG = "-post"
            var retry = RETRY_COUNT
            var httpResponse = HttpResponse()
            while (retry-- >= 0) {
                try {
                    httpResponse = restPost(baseUrl, qString, postParam, contentType, token);
                    break
                }
                catch (e: Exception) {
                    Log.e(TAG+SUBTAG, "got exception " + e)
                    if (retry == 0) {
                        httpResponse.responseCode = -1
                        break
                    }
                    Thread.sleep(RETRY_WAIT)
                }
            }
            return httpResponse
        }

        @JvmStatic fun restPost(baseUrl: String, qString: String, postParam: ByteArray, pContentType: String, token: String?): HttpResponse {
            val SUBTAG = "-restPost(ByteArray)"
            Log.e(TAG+SUBTAG, "Query String: $qString")
            Log.e(TAG+SUBTAG, "postParam: $postParam")

            val mURL = URL("${baseUrl}${qString}")
            var httpResponse = HttpResponse()

            throttle()

            try {
                val httpUrlConn : HttpURLConnection
                httpUrlConn = mURL.openConnection() as HttpURLConnection

                with(httpUrlConn) {
                    requestMethod = "POST"

                    setRequestProperty("Content-Type", pContentType)
                    if (token != "")
                        setRequestProperty("Authorization", "Bearer ${token}")

                    connectTimeout = CONNECT_TIMEOUT
                    readTimeout = READ_TIMEOUT
                    setRequestProperty("User-Agent", DEFAULT_AGENT + AGENT_SUFFIX)

                    if (postParam.size > 0) {
                        val wr = DataOutputStream(getOutputStream())
                        wr.write(postParam)
                        wr.flush()
                    }

                    Log.e(TAG+SUBTAG, "URL : $url")
                    Log.e(TAG+SUBTAG, "Response Code : $responseCode")

                    httpResponse.responseCode = responseCode

                    val streamUsed = if (responseCode == 200) inputStream else errorStream

                    if (contentType != null) {
                        if ((contentType ?: "").contains("json") || (contentType ?: "").contains("text")) {
                            BufferedReader(InputStreamReader(streamUsed)).use {
                                val response = StringBuffer()
                                var inputLine = it.readLine()
                                while (inputLine != null) {
                                    response.append(inputLine)
                                    inputLine = it.readLine()
                                }

                                Log.d(TAG+SUBTAG, "Response : '${response}'")

                                httpResponse.response = response.toString()
                            }
                        } else
                            if (contentLength > 0) {
                                var array = ByteArray(contentLength)
                                val dataIs = DataInputStream(streamUsed)
                                dataIs.readFully(array)
                                httpResponse.byteResponse = array
                                httpResponse.response = array.toString(Charsets.UTF_8)
                            } else {
                                val dataIs = DataInputStream(streamUsed)
                                val size = 1024 * 1024
                                val array = ByteArray(size)
                                val r = dataIs.read(array)
                                if (r != -1) {
                                    val newarray = array.copyOf(r)
                                    httpResponse.response = newarray.toString(Charsets.UTF_8)
                                    httpResponse.contentLength = httpResponse.response.length
                                } else
                                    Log.e(TAG+SUBTAG, "No data")
                            }
                    }

                    deThrottle()
                    return httpResponse;
                }
            }
            catch (e: java.lang.Exception) {
                throw (e)
            }
        }

        @JvmStatic fun get(url: String): HttpResponse {
            return get(url, "", "", 0L)
        }

        @JvmStatic fun get(url: String, ifNotModified: Long): HttpResponse {
            return get(url, "", "", ifNotModified)
        }

        @JvmStatic fun get(url: String, token: String?, ifNotModified: Long): HttpResponse {
            return get(url, "", token, ifNotModified)
        }

        @JvmStatic fun get(baseUrl: String, qString: String, token: String?, ifNotModified: Long): HttpResponse {
            var retry = RETRY_COUNT
            var httpResponse = HttpResponse()
            while (retry-- >= 0) {
                try {
                    httpResponse = restGet(baseUrl, qString, token,ifNotModified)
                    break
                }
                catch (e: Exception) {
                    if (retry == 0) {
                        httpResponse.responseCode = -1
                        break
                    }
                    Thread.sleep(RETRY_WAIT)
                }
            }
            return httpResponse
        }

        @JvmStatic fun restGet(baseUrl: String, qString: String, token: String?, ifNotModified: Long): HttpResponse {
            val SUBTAG = "-restGet"
            val mURL = URL("${baseUrl}${qString}")
            val httpResponse = HttpResponse()

            throttle()

            try {
                val httpUrlConn : HttpURLConnection
                httpUrlConn = mURL.openConnection() as HttpURLConnection

                with(httpUrlConn) {
                    requestMethod = "GET"

                    if (token != "")
                        setRequestProperty("Authorization", "Bearer ${token}")

                    connectTimeout = CONNECT_TIMEOUT
                    readTimeout    = READ_TIMEOUT

                    if (ifNotModified > 0L) {
                        this.ifModifiedSince = ifNotModified // - 1000*60*60*24*3
                    }

                    setRequestProperty("User-Agent", DEFAULT_AGENT + AGENT_SUFFIX)

                    Log.d(TAG+SUBTAG, "URL : $url")
                    if (responseCode == 200)
                        Log.d(TAG+SUBTAG, "Response Code : $responseCode")
                    else
                        Log.e(TAG+SUBTAG, "Response Code : $responseCode")

                    httpResponse.responseCode = responseCode
                    httpResponse.contentLength = contentLength
                    httpResponse.contentType = contentType ?: ""

                    if (contentType != null) {
                        val streamUsed = if (responseCode == 200) inputStream else errorStream

                        if ((contentType ?: "").contains("json") || (contentType ?: "").contains("text")) {
                            BufferedReader(InputStreamReader(streamUsed)).use {
                                val response = StringBuffer()
                                var inputLine = it.readLine()

                                while (inputLine != null) {
                                    response.append(inputLine)
                                    inputLine = it.readLine()
                                }

                                Log.d(TAG+SUBTAG, "Response : $response")
                                httpResponse.response = response.toString()
                            }
                        }
                        else
                            if (contentLength > 0) {
                                val array = ByteArray(contentLength)
                                val dataIs = DataInputStream(streamUsed)
                                dataIs.readFully(array)
                                httpResponse.byteResponse = array
                                httpResponse.response = array.toString(Charsets.UTF_8)
                            }
                            else {
                                val dataIs = DataInputStream(streamUsed)
                                val size = 1024 * 1024
                                val array = ByteArray(size)
                                val r = dataIs.read(array)
                                if (r != -1) {
                                    val newarray = array.copyOf(r)
                                    httpResponse.response = newarray.toString(Charsets.UTF_8)
                                    httpResponse.contentLength = httpResponse.response.length
                                }
                                else
                                    Log.e(TAG+SUBTAG, "No data")
                            }
                    }

                    deThrottle()
                    return httpResponse
                }
            }
            catch (e: java.lang.Exception) {
                throw (e)
            }
        }

        @JvmStatic fun delete(baseUrl: String, qString: String, token: String?): HttpResponse {
            var retry = RETRY_COUNT
            var httpResponse = HttpResponse()
            while (retry-- >= 0) {
                try {
                    httpResponse = restDelete(baseUrl, qString, token);
                    break
                }
                catch (e: Exception) {
                    if (retry == 0) {
                        httpResponse.responseCode = -1
                        break
                    }
                    Thread.sleep(RETRY_WAIT)
                }
            }
            return httpResponse
        }

        @JvmStatic fun restDelete(baseUrl: String, qString: String, token: String?): HttpResponse {
            val SUBTAG = "-restDelete"
            val mURL = URL("${baseUrl}${qString}")
            val httpResponse = HttpResponse()

            val httpUrlConn : HttpURLConnection
            httpUrlConn = mURL.openConnection() as HttpURLConnection

            with(httpUrlConn) {
                requestMethod = "DELETE"
                if (token != "")
                    setRequestProperty("Authorization", "Bearer ${token}")

                connectTimeout = 10000
                readTimeout = 15000

                setRequestProperty("User-Agent", DEFAULT_AGENT + AGENT_SUFFIX)

                Log.d(TAG+SUBTAG, "URL : $url")
                Log.d(TAG+SUBTAG, "Response Code : $responseCode")
                httpResponse.responseCode = responseCode
                httpResponse.contentLength = contentLength
                httpResponse.contentType = contentType ?: ""

                if (contentType == null)
                    return httpResponse

                val streamUsed = if (responseCode == 200) inputStream else errorStream

                if ((contentType ?: "").contains("json")) {
                    BufferedReader(InputStreamReader(streamUsed)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        Log.d(TAG+SUBTAG, "Response : $response")

                        httpResponse.response = response.toString()
                    }
                }
                else {
                    val array = ByteArray(contentLength)
                    val dataIs = DataInputStream(streamUsed)
                    dataIs.readFully(array)
                    httpResponse.byteResponse = array
                }
                return httpResponse
            }
        }

        fun getStream(baseUrl: String, qString: String, token: String?): InputStream? {
            val SUBTAG = "-getStream"
            val mURL = URL("${baseUrl}${qString}")
            var responseStream: InputStream? = null;

            with(mURL.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                if (token != "")
                    setRequestProperty("Authorization", "Bearer ${token}")

                Log.d(TAG+SUBTAG, "URL : $url")
                Log.d(TAG+SUBTAG, "Response Code : $responseCode")

                if (responseCode == 200) {
                    responseStream = inputStream
                }
                return responseStream
            }
        }
    }
}

class HttpResponse {
    var responseCode: Int = 0
    var contentType = ""
    var contentLength = 0
    var response: String = ""
    var byteResponse: ByteArray = byteArrayOf(0x00)
}


