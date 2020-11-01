package com.roman.movieApp.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.movieApp.repository.MovieDetail
import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieDetailViewModel(val movieRepository: MovieRepository, val movieId: String) :
    ViewModel() {

    private val movieDetailStateObserver = MutableLiveData<State<MovieDetail>>()

    init {
        movieDetailStateObserver.postValue(State.Loading)

        viewModelScope.launch(context = Dispatchers.IO) {
            movieDetailStateObserver.postValue(State.Loading)
            try {
                movieRepository.getMovieDetail(movieId).collect {
                    movieDetailStateObserver.postValue(State.Loaded(it))
                }
            } catch (e: Exception) {
                movieDetailStateObserver.postValue(State.Error(e))
            }
        }
    }

    fun observeMovieDetail(): LiveData<State<MovieDetail>> = movieDetailStateObserver
}
