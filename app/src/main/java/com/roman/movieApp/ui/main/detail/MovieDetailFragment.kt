package com.roman.movieApp.ui.main.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.roman.movieApp.R
import com.roman.movieApp.repository.MovieDetail
import com.roman.movieApp.util.State
import com.roman.movieApp.util.imgBaseUrlBig
import com.roman.movieApp.util.isVisible
import com.roman.movieApp.util.withModels
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieDetailFragment : Fragment() {
    private val viewModel: MovieDetailViewModel by viewModel {
        parametersOf(
            arguments?.getString(
                MOVIE_ID_KEY
            )
        )
    }

    companion object {
        private val MOVIE_ID_KEY = "movie_id"

        fun withArguments(movieId: String) = MovieDetailFragment().apply {
            arguments = bundleOf(MOVIE_ID_KEY to movieId)
        }
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
        viewModel.observeMovieDetail().observe(viewLifecycleOwner, Observer { state ->
            loading_layout.isVisible = state is State.Loading

            when (state) {
                is State.Loaded -> showMovie(state.data)
                is State.Error -> Toast.makeText(context, "Loading failed: ${state.error.message}", Toast.LENGTH_LONG).show()
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
        detailrecyclerview.adapter.apply {
            val menu = toolbar.menu
        }
        
        images_title.isVisible = movie.images?.backdrops?.isNotEmpty() ?: false
        detailrecyclerview.withModels {
            movie.images?.backdrops?.forEach {
                image {
                    id(it.filePath)
                    imgUrl(imgBaseUrlBig + it.filePath)
                }
            }
        }
    }
}