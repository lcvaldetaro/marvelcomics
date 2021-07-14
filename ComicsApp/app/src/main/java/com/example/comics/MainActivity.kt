package com.example.comics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var thisActivity : MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        thisActivity = this

        thread {
            App.marvelResults = NetworkRequests.getComics()
            if (App.marvelResults != null) {
                Log.e(TAG, App.marvelResults.toString())

                for (Comic in App.marvelResults!!.data!!.results!!) {
                    Log.d(TAG, "Title=${Comic.title}")
                    if (Comic.images != null && Comic.images.size > 0) {
                        val image = Comic.images[0]
                        Log.d(TAG, "image=${image.path}.${image.extension}")
                    }
                }
            } else {
                Log.e(TAG, "got error")
                toast("Failed to get the data from the Marvel server!!!!!!!!!!\nCall Spyder-Man")
            }
        }
    }
}