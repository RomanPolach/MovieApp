package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    fun getMoviesPage(): Flow<List<Result>?>

    fun getMovieDetail(movieId: String): Flow<MovieDetail>

    fun search(movie: String): Flow<List<Result>?>
}