package com.roman.movieApp.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.movieApp.repository.MovieDetail
import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.util.State
import com.roman.movieApp.util.launchRequestWithState

class MovieDetailViewModel(val movieRepository: MovieRepository, val movieId: String) :
    ViewModel() {

    private val movieDetailStateObserver = MutableLiveData<State<MovieDetail>>()

    init {
        viewModelScope.launchRequestWithState(request = { movieRepository.getMovieDetail(movieId) }, mutableLiveData = movieDetailStateObserver)
    }

    fun observeMovieDetail(): LiveData<State<MovieDetail>> = movieDetailStateObserver
}