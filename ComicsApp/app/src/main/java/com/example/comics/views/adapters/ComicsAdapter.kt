package com.example.comics.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.comics.App
import com.example.comics.R
import com.example.comics.loadImageIntoView
import com.example.comics.models.Comic

class ComicsAdapter(private var comics : List<Comic>, private val itemClick: (Comic) -> Unit) : RecyclerView.Adapter<ComicsAdapter.ViewHolder>() {
    companion object {
        private val TAG = ComicsAdapter::class.java.getSimpleName()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder pos $position")
        holder.bindMakers(App.marvelResults!!.data!!.results, position)
    }

    override fun getItemCount(): Int = App.marvelResults!!.data!!.results!!.size

    class ViewHolder(view: View, private val itemClick:  (Comic) -> Unit) : RecyclerView.ViewHolder(view) {
        private var iconView = view.findViewById<ImageView>(R.id.item_icon)
        private val descriptionView = view.findViewById<TextView>(R.id.item_description)

        fun bindMakers(comic: List<Comic>?, position: Int) {
            Log.d(TAG, "View Holder position $position")
            with(comic!![position]) {
                if (images != null && images.size > 0) {
                    loadImageIntoView(itemView.context, "${images[0].path}.${images[0].extension}", iconView)
                }
                descriptionView.text = title
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }


}