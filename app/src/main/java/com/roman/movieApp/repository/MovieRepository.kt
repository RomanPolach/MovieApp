package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    suspend fun getMovies(): Flow<List<Result>?>
}