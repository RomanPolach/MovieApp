package com.roman.movieApp.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.roman.movieApp.R
import com.roman.movieApp.repository.Movie
import com.roman.movieApp.util.imgBaseUrl
import com.squareup.picasso.Picasso

class SearchAdapter(private val onItemClicked: (Movie) -> Unit) : ListAdapter<Movie, SearchAdapter.ViewHolder>(MovieComparator) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView
        val titleView: TextView
        val score: TextView
        val actors: TextView

        init {
            imgPoster = view.findViewById(R.id.img_logo)
            titleView = view.findViewById(R.id.txt_title)
            score = view.findViewById(R.id.txt_score)
            actors = view.findViewById(R.id.txt_genres)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        holder.itemView.setOnClickListener {
            movie.let {
                onItemClicked(movie)
            }
        }
        Picasso.get().load(imgBaseUrl + movie.posterPath).into(holder.imgPoster)
        holder.titleView.text = movie.title ?: ""
        holder.score.text = movie.voteAverage?.toString() ?: ""
        holder.actors.text = movie.genres.joinToString(separator = ", ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_layout, parent, false)
        return ViewHolder(view)
    }
}

object MovieComparator : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}