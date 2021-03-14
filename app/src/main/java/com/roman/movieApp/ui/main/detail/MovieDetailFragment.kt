package com.roman.movieApp.ui.main.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.roman.movieApp.R
import com.roman.movieApp.model.api.MovieDetail
import com.roman.movieApp.util.State
import com.roman.movieApp.util.handleError
import com.roman.movieApp.util.imgBaseUrlBig
import com.roman.movieApp.util.isVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieDetailFragment : Fragment() {
    val args: MovieDetailFragmentArgs by navArgs()

    private val viewModel: MovieDetailViewModel by viewModel {
        parametersOf(args.movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        detailrecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.observeMovieDetail().observe(viewLifecycleOwner, Observer { state ->
            loading_layout.isVisible = state is State.Loading

            when (state) {
                is State.Loaded -> showMovie(state.data)
                is State.Error -> handleError(state.error)
            }
        })
    }

    private fun showMovie(movie: MovieDetail) {
        collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT)
        txt_title.text = movie.title
        toolbar.title = "Movie details"
        movie.voteAverage?.toFloat()?.let {
            ratingbar.rating = it
        }
        Picasso.get().load(imgBaseUrlBig + movie.backdropPath).into(imageview_logo)
        txt_released.text = "Released: ${movie.releaseDate}"
        txt_summary.text = movie.overview
        txt_origin.text = "Language: ${movie.originalLanguage}"

        images_title.isVisible = movie.images?.backdrops?.isNotEmpty() ?: false
        val imgUrls = movie?.images?.backdrops?.map { imgBaseUrlBig + it.filePath } ?: emptyList()
        detailrecyclerview.adapter = ImageAdapter(imgUrls)
    }
}