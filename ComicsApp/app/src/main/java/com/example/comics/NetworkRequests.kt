package com.example.comics
import android.util.Log
import com.cacau.slotCarsApp.models.ComicDataWrapper
import com.google.gson.Gson
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class NetworkRequests {
    companion object {
        private val TAG = NetworkRequests::class.java.getSimpleName()

        fun md5(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return (BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')).toLowerCase()
        }

        @JvmStatic fun getComics() : ComicDataWrapper? {
            var result: ComicDataWrapper? = null
            val ts = Date().time.toString()

            val md5Hash = md5("${ts}${Constants.MARVEL_PRIVATE_KEY}${Constants.MARVEL_PUBLIC_KEY}")
            val url = "${Constants.MARVEL_QUERY_STRING}${Constants.MARVEL_PUBLIC_KEY}&hash=${md5Hash}&ts=${ts}"

            val httpresponse = Network.get(url)

            Log.d(TAG, "response=${httpresponse.response}")
            if (httpresponse.responseCode == 200)
                result = Gson().fromJson(httpresponse.response, ComicDataWrapper::class.java)

            return result
        }
    }
}