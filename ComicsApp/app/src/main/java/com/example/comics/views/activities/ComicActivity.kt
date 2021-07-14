package com.example.comics.views.activities

import android.os.Bundle
import android.text.method.ScrollingMovementMethod

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager



import com.example.comics.App
import com.example.comics.R
import com.example.comics.loadImageIntoView
import com.example.comics.views.adapters.PicturesAdapter
import kotlinx.android.synthetic.main.comic.*

class ComicActivity: AppCompatActivity() {
    private val TAG = ComicActivity::class.java.getSimpleName()
    private var thisComicActivity : ComicActivity? = null
    private var adapter : PicturesAdapter? = null
    private var comic = App.currentComic

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        thisComicActivity = this

        setupViews     ()
        setupGlobals   ()
        setupListeners ()
    }

    fun setupViews () {
        setContentView(R.layout.comic)
        description!!.movementMethod = ScrollingMovementMethod()
        icon!!.maxHeight = 600
    }

    fun setupGlobals () {

        title = comic!!.title

        description!!.text = "${comic!!.title}\n${comic!!.variantDescription}"

        if (comic!!.images != null && comic!!.images!!.size > 0) {
            loadImageIntoView(this, "${comic!!.images!![0].path}.${comic!!.images!![0].extension}", icon!!)
            pics_list.layoutManager = GridLayoutManager(this, 3)
            val images: ArrayList<String> = ArrayList()
            if (comic!!.images!!.size > 1) {
                var count = 0
                for (image in comic!!.images!!) {
                    if (count < 1)
                        continue
                    count++
                    val completeName = "${image.path}.${image.extension}"
                    images.add(completeName)
                }
                adapter = PicturesAdapter(images, true) { }
                pics_list.adapter = adapter!!
            }
        }
    }

    fun setupListeners () {
    }
}