package com.roman.movieApp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.movieApp.repository.Movie
import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.util.State
import com.roman.movieApp.util.launchRequestWithState
import com.roman.movieApp.util.setError
import com.roman.movieApp.util.setLoaded
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

class MainViewModel(val movieRepository: MovieRepository) : ViewModel() {

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
            movieListStateObserver.setLoaded(it)
        }
    }.launchIn(viewModelScope)

    init {
        loadMovies()
    }

    fun loadMovies() {
        if (searchChanel.value.isNotBlank()) {
            return
        }

        viewModelScope.launchRequestWithState({ movieRepository.getMoviesPage() }, movieListStateObserver)
    }

    fun observeMovies(): LiveData<State<List<Movie>>> = movieListStateObserver

    fun setSearchTerm(searchTerm: String) {
        searchChanel.offer(searchTerm)
    }

    fun resetSearch() {
        searchChanel.offer("")
        loadMovies()
    }
}