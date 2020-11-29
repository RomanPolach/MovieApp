package com.roman.movieApp.ui.main

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fondesa.recyclerviewdivider.RecyclerViewDivider
import com.roman.movieApp.MainActivity
import com.roman.movieApp.R
import com.roman.movieApp.repository.Result
import com.roman.movieApp.ui.main.detail.MovieDetailFragment
import com.roman.movieApp.ui.main.epoxy.empty
import com.roman.movieApp.ui.main.epoxy.movieList
import com.roman.movieApp.util.*
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
        setToolbar()
        addEpoxyDivider()

        epoxyRecyclerView.addOnScrollListener((InfiniteScrollListener({
            viewModel.loadMovies()
        }, epoxyRecyclerView.layoutManager as LinearLayoutManager)))
        epoxyRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx < 0 || dy > 0) {
                    hideKeyboard()
                }
            }
        })

        viewModel.observeMovies().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Loading -> {
                    showProgress(true)
                }
                is State.Loaded -> {
                    showProgress(false)
                    if (state.data.isEmpty()) {
                        showEmptyLayout()
                    } else {
                        showMovies(state.data)
                    }
                }
                is State.Error -> {
                    showProgress(false)
                    handleError(state.error)
                }
            }
        })
    }


    fun addEpoxyDivider() {
        epoxyRecyclerView.addItemDecoration(
            RecyclerViewDivider.with(requireContext())
                .inset(16.dp, 16.dp)
                .size(2.dp)
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
                    onClick {
                        it.id.let { movieId ->
                            (activity as MainActivity).openFragment(MovieDetailFragment.withArguments(movieId))
                        }
                    }
                }
            }
        }
    }

    fun showProgress(show: Boolean) {
        progressBar.isVisible = show
    }

    fun showEmptyLayout() {
        epoxyRecyclerView.withModels {
            empty {
                id("empty")
            }
        }
    }

    private fun setToolbar() {
        tooolbar_main.inflateMenu(R.menu.main_menu)
        tooolbar_main.title = getString(R.string.popular_movies)

        val myActionMenuItem = tooolbar_main.menu.findItem(R.id.app_bar_search)
        val searchView = myActionMenuItem.actionView as SearchView

        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        val mCloseButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.WHITE)
        searchEditText.setHintTextColor(Color.WHITE)
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon).setColorFilter(
            Color.WHITE,
            PorterDuff.Mode.SRC_IN
        )
        searchIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        mCloseButton.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchTerm: String): Boolean {
                viewModel.setSearchTerm(searchTerm)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(searchTerm: String?): Boolean {
                if (searchTerm.isNullOrBlank()) {
                    viewModel.resetSearch()
                    hideKeyboard()
                }
                return true
            }
        })
    }

    fun hideKeyboard() {
        val imm: InputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}