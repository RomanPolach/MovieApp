package com.roman.movieApp.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.movieApp.repository.MovieDetail
import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.util.State
import com.roman.movieApp.util.setError
import com.roman.movieApp.util.setLoaded
import com.roman.movieApp.util.setLoading
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieDetailViewModel(val movieRepository: MovieRepository, val movieId: String) :
    ViewModel() {

    private val movieDetailStateObserver = MutableLiveData<State<MovieDetail>>()

    init {
        movieDetailStateObserver.setLoading()

        viewModelScope.launch {
            try {
                movieRepository.getMovieDetail(movieId).collect {
                    movieDetailStateObserver.setLoaded(it)
                }
            } catch (e: Exception) {
                movieDetailStateObserver.setError(e)
            }
        }
    }

    fun observeMovieDetail(): LiveData<State<MovieDetail>> = movieDetailStateObserver
}