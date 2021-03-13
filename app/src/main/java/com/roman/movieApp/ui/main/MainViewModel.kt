package com.roman.movieApp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.roman.movieApp.repository.Movie
import com.roman.movieApp.repository.MoviePagingSource
import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.util.State
import com.roman.movieApp.util.setError
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

class MainViewModel(val movieRepository: MovieRepository) : ViewModel() {

    val searchedMovies = MutableLiveData<List<Movie>>()

    private val movieListStateObserver = MutableLiveData<State<List<Movie>>>()
    private val searchChanel = ConflatedBroadcastChannel("")
    private val searchObserver = searchChanel.asFlow().filter {
        it.isNotBlank()
    }.flatMapLatest {
        movieRepository.search(it)
    }.catch {
        movieListStateObserver.setError(it)
    }.onEach { results ->
        results?.let {
            searchedMovies.postValue(results)
        }
    }.launchIn(viewModelScope)

    var movies: Flow<PagingData<Movie>> = Pager(PagingConfig(pageSize = 20, prefetchDistance = 10)) {
        MoviePagingSource(movieRepository)
    }.flow

    fun loadMovies() {
        if (searchChanel.value.isNotBlank()) {
            return
        }
        movies = Pager(PagingConfig(pageSize = 20, prefetchDistance = 10)) {
            MoviePagingSource(movieRepository)
        }.flow
    }

    fun observeSearchResults(): LiveData<List<Movie>> = searchedMovies

    fun setSearchTerm(searchTerm: String) {
        searchChanel.offer(searchTerm)
    }

    fun resetSearch() {
        searchChanel.offer("")
    }
}