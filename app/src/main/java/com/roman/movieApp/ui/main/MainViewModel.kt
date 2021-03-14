package com.roman.movieApp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.roman.movieApp.model.api.Movie
import com.roman.movieApp.model.repository.MovieRepository
import com.roman.movieApp.ui.main.paging.MoviePagingSource
import com.roman.movieApp.util.State
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

class MainViewModel(val movieRepository: MovieRepository) : ViewModel() {

    private val searchedListStateObserver = MutableStateFlow<State<List<Movie>>>(State.Empty)
    private val searchChanel = ConflatedBroadcastChannel("")
    val searchResultsFlow = searchChanel.asFlow().filter {
        it.isNotBlank()
    }.flatMapLatest {
        movieRepository.search(it)
    }.catch {
        searchedListStateObserver.value = State.Error(it)
    }.onEach { results ->
        results?.let {
            searchedListStateObserver.value = State.Loaded(results)
        }
    }

    init {
        searchResultsFlow.launchIn(viewModelScope)
    }

    var movies: Flow<PagingData<Movie>> = Pager(PagingConfig(pageSize = 20, prefetchDistance = 10)) {
        MoviePagingSource(movieRepository)
    }.flow

    fun observeSearchResults(): StateFlow<State<List<Movie>>> = searchedListStateObserver

    fun setSearchTerm(searchTerm: String) {
        searchChanel.offer(searchTerm)
    }

    fun resetSearch() {
        searchChanel.offer("")
    }
}