package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    suspend fun getMoviesPage(): List<Result>

    suspend fun getMovieDetail(movieId: String): MovieDetail

    fun search(movie: String): Flow<List<Result>?>
}