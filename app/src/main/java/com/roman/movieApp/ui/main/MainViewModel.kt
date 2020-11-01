package com.roman.movieApp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.repository.Result
import com.roman.movieApp.util.State
import com.roman.movieApp.util.setError
import com.roman.movieApp.util.setLoading
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(val movieRepository: MovieRepository) : ViewModel() {

    private val movieListStateObserver = MutableLiveData<State<List<Result>>>()
    private val searchChanel = ConflatedBroadcastChannel<String>()
    private val searchObserver = searchChanel.asFlow().filter {
        it.isNotBlank()
    }.flatMapLatest {
        movieRepository.search(it)
    }.catch {
        movieListStateObserver.setError(it)
    }.onEach { results ->
        results?.let {
            movieListStateObserver.postValue(if (results.isEmpty()) State.Empty else State.Loaded(it))
        }
    }.launchIn(viewModelScope)

    init {
        loadMovies()
    }

    fun loadMovies() {
        movieListStateObserver.setLoading()

        viewModelScope.launch {
            movieRepository.getMovies().onEach { results ->
                results?.let {
                    movieListStateObserver.postValue(
                        if (results.isEmpty()) State.Empty else State.Loaded(
                            it
                        )
                    )
                }
            }.catch {
                movieListStateObserver.setError(it)
            }.collect()
        }
    }

    fun observeMovies(): LiveData<State<List<Result>>> = movieListStateObserver

    fun setSearchTerm(searchTerm: String) {
        searchChanel.offer(searchTerm)
    }

    fun resetSearch() {
        searchChanel.offer("")
        loadMovies()
    }
}