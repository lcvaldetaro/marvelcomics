package com.example.comics.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.comics.R


class PicturesAdapter (private val pictures : List<String>,  private val ignoreFirst: Boolean, private val itemClick: (String) -> Unit) : RecyclerView.Adapter<PicturesAdapter.ViewHolder>(){
    companion object {
        private val TAG = PicturesAdapter::class.java.getSimpleName()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image, parent, false)
        return PicturesAdapter.ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: PicturesAdapter.ViewHolder, position: Int) {
        Log.d(TAG, "Processing position ${position}")
        holder.bindPictures(pictures, position, ignoreFirst)
    }

    override fun getItemCount(): Int =pictures.size

    class ViewHolder(view: View, private val itemClick: (String) -> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private var iconView = view.findViewById<ImageView>(R.id.image)

        fun bindPictures(pictures: List<String>, position: Int, ignoreFirst: Boolean) {
            with(pictures[position]) {
                val images = pictures.reversed()

                var last = images.size -1
                if (!ignoreFirst)
                    last = images.size

                if (position < last) {
                    val picture = images[position]
                    if (!picture.equals("")) {
                        //val picUrl = NetworkRequests.getBaseUrl() + picture
                        //loadImageIntoView(itemView.ctx, picUrl, iconView)
                    }
                    itemView.setOnClickListener { itemClick(picture) }
                }
            }
        }
    }

}