package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    suspend fun getMovies(): Flow<List<Result>?>

    suspend fun getMovieDetail(movieId: String): Flow<MovieDetail>

    suspend fun search(movie: String): Flow<List<Result>?>
}