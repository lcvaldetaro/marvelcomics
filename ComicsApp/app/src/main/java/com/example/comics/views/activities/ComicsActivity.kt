package com.example.comics.views.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.comics.*
import com.example.comics.views.adapters.ComicsAdapter
import kotlinx.android.synthetic.main.main.*
import kotlin.concurrent.thread


class ComicsActivity : AppCompatActivity() {
    private val TAG = ComicsActivity::class.java.getSimpleName()
    private var thisActivity : ComicsActivity? = null
    private var adapter : ComicsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivity = this

        setupViews    ()
        setupListeners()

        comics_list!!.layoutManager = GridLayoutManager(this, 1)
        thread {
            App.marvelResults = NetworkRequests.getComics()
            if (App.marvelResults != null) {
                Log.e(TAG, App.marvelResults.toString())

                for (Comic in App.marvelResults!!.data!!.results!!) {
                    Log.d (TAG, "Title=${Comic.title}" )
                    if (Comic.images != null && Comic.images.size > 0) {
                        val image = Comic.images[0]
                        Log.d (TAG, "image=${image.path}.${image.extension}")
                    }
                }

                thisActivity!!.runOnUiThread{
                    if (App.marvelResults != null) {
                        adapter = ComicsAdapter(App.marvelResults!!.data!!.results!!) {
                            App.currentComic = it
                            goToActivity(thisActivity!!, ComicActivity::class.java)
                        }
                        comics_list.adapter = adapter
                    }
                    setupTitle()
                }
            }
            else {
                Log.e(TAG, "got error")
                toast ("Failed to get the data from the Marvel server!!!!!!!!!!\nCall Spyder-Man")
            }
        }
    }

    fun setupViews () {
        setContentView(R.layout.main)
        title         = "Loading... Please wait."
    }

    fun setupTitle () {
        thread {
            thisActivity!!.runOnUiThread {
                thisActivity!!.title = "List of marvel comics"
                headerName!!.text = title
            }
        }
    }

    fun setupListeners () {

    }

}
