package com.roman.movieApp.model.repository

import com.roman.movieApp.model.api.Movie
import com.roman.movieApp.model.api.MovieDetail
import com.roman.movieApp.model.api.RequestResult
import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    suspend fun getMovieDetail(movieId: String): MovieDetail

    fun search(movie: String): Flow<List<Movie>?>

    suspend fun getMoviesPage(page: Int): RequestResult
}