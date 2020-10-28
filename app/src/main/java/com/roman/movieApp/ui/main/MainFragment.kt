package com.roman.movieApp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fondesa.recyclerviewdivider.RecyclerViewDivider
import com.roman.movieApp.R
import com.roman.movieApp.repository.Result
import com.roman.movieApp.ui.main.epoxy.movieList
import com.roman.movieApp.util.State
import com.roman.movieApp.util.isVisible
import com.roman.movieApp.util.px
import com.roman.movieApp.util.withModels
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEpoxyDivider()
        activity?.title = getString(R.string.movie_list_title)

        viewModel.observeMovies().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Empty -> {
                    showProgress(false)
                    showEmptyLayout(true)
                }
                is State.Loading -> {
                    showProgress(true)
                    showEmptyLayout(false)
                }
                is State.Loaded -> {
                    showProgress(false)
                    showEmptyLayout(false)
                    showMovies(state.data)
                }
                is State.Error -> {
                    showProgress(false)
                    showEmptyLayout(false)
                    Toast.makeText(context, state.error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun addEpoxyDivider() {
        epoxyRecyclerView.addItemDecoration(
            RecyclerViewDivider.with(requireContext())
                .inset(16.px, 16.px)
                .size(2.px)
                .hideLastDivider()
                .color(Color.BLACK)
                .build()
        )
    }


    private fun showMovies(movies: List<Result>) {
        epoxyRecyclerView.withModels {
            movies.forEach {
                movieList {
                    id(it.id)
                    movie(it)
                }
            }
        }
    }

    fun showProgress(show: Boolean) {
        progressBar.isVisible = show
    }

    fun showEmptyLayout(show: Boolean) {
        empty_layout.isVisible = show
    }
}