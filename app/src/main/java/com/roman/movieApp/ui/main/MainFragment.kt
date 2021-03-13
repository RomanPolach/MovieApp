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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.recyclerviewdivider.RecyclerViewDivider
import com.roman.movieApp.R
import com.roman.movieApp.ui.main.adapters.MoviesAdapter
import com.roman.movieApp.ui.main.adapters.SearchAdapter
import com.roman.movieApp.util.dp
import com.roman.movieApp.util.handleError
import com.roman.movieApp.util.isVisible
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()

    val searchAdapter = SearchAdapter { movie ->
        openMovieDetail(movie.id)
    }
    private val moviesAdapter = MoviesAdapter { movie ->
        openMovieDetail(movie.id)
    }

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

        recyclerView.layoutManager = LinearLayoutManager(context)
        loadMovieList()

        viewModel.observeSearchResults().observe(viewLifecycleOwner, Observer() { movies ->
            recyclerView.adapter = searchAdapter
            searchAdapter.submitList(movies)
        })

        viewLifecycleOwner.lifecycleScope.launch {
            moviesAdapter.loadStateFlow.collectLatest { loadStates ->
                val state = loadStates.refresh
                showProgress(state is LoadState.Loading)
                if (state is LoadState.Error) {
                    handleError(state.error)
                }
            }
        }
    }

    private fun loadMovieList(isReloading: Boolean = false) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collectLatest {
                recyclerView.adapter = moviesAdapter
                moviesAdapter.submitData(it)
            }
        }
        if (isReloading) {
            viewModel.loadMovies()
        }
    }

    fun addEpoxyDivider() {
        recyclerView.addItemDecoration(
            RecyclerViewDivider.with(requireContext())
                .inset(16.dp, 16.dp)
                .size(2.dp)
                .hideLastDivider()
                .color(Color.BLACK)
                .build()
        )
    }

    fun openMovieDetail(movieId: String) {
        findNavController().navigate(MainFragmentDirections.openMovieDetail(movieId))
    }

    fun showProgress(show: Boolean) {
        progressBar.isVisible = show
    }

    fun showEmptyLayout() {
        // TODO - implement empty layout
//        epoxyRecyclerView.withModels {
//            empty {
//                id("empty")
//            }
//        }
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
                    loadMovieList(true)
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