package com.roman.movieApp.ui.main.epoxy

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.roman.movieApp.R
import com.roman.movieApp.repository.Result
import com.roman.movieApp.util.imgBaseUrl
import com.squareup.picasso.Picasso


@EpoxyModelClass(layout = R.layout.movie_list_layout)
abstract class MovieListModel : EpoxyModelWithHolder<MovieListModel.Holder>() {
    @EpoxyAttribute
    var movie: Result? = null

    @EpoxyAttribute
    var onClick: (movie: Result) -> Unit = {}

    override fun bind(holder: Holder) {
        holder.view.setOnClickListener {
            movie?.let {
                onClick(it)
            }
        }
        Picasso.get().load(imgBaseUrl + movie?.posterPath).into(holder.imgPoster)
        holder.titleView.text = movie?.title ?: ""
        holder.score.text = movie?.voteAverage?.toString() ?: ""
        holder.actors.text = movie?.genres?.joinToString(separator = ", ") ?: ""
    }

    class Holder : KotlinEpoxyHolder() {
        val imgPoster by bind<ImageView>(R.id.img_logo)
        val titleView by bind<TextView>(R.id.txt_title)
        val score by bind<TextView>(R.id.txt_score)
        val actors by bind<TextView>(R.id.txt_genres)
    }
}