package com.roman.movieApp.ui.main.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.roman.movieApp.R
import com.squareup.picasso.Picasso

class ImageAdapter(var imgUrls: List<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView

        init {
            imageView = view.findViewById((R.id.image))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_image_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imgUrls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(imgUrls.get(position)).into(holder.imageView)
    }

}