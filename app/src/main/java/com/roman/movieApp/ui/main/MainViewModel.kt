package com.roman.movieApp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.repository.Result
import com.roman.movieApp.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(val movieRepository: MovieRepository) : ViewModel() {

    private val movieListStateObserver = MutableLiveData<State<List<Result>>>()

    init {
        movieListStateObserver.postValue(State.Loading)

        viewModelScope.launch(context = Dispatchers.IO) {
            movieRepository.getMovies().catch {
                State.Error(it)
            }.onEach { results ->
                results?.let {
                    movieListStateObserver.postValue(
                        if (results.isEmpty()) State.Empty else State.Loaded(
                            it
                        )
                    )
                }
            }.collect()
        }
    }

    fun observeMovies(): LiveData<State<List<Result>>> = movieListStateObserver
}