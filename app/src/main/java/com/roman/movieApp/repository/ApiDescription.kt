package com.roman.movieApp.repository

import retrofit2.http.GET
import retrofit2.http.Query


interface ApiDescription {

    @GET("discover/movie?")
    suspend fun getMovies(
        @Query("api_key") apiKEY: String,
        @Query("language") language: String,
        @Query("primary_release_year") year: String
    ): SearchResult

    @GET("genre/movie/list")
    suspend fun getGenres(@Query("api_key") apiKEY: String): Genres

}