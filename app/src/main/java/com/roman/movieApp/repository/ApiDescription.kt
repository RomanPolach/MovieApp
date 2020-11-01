package com.roman.movieApp.repository

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiDescription {
    // ApiKey for requests is inserted to queries automaticaly through interceptor in RemoteModule
    @GET("discover/movie?")
    suspend fun getMovies(
        @Query("language") language: String,
        @Query("primary_release_year") year: String,
        @Query("sort_by") sortBy: String
    ): SearchResult

    @GET("genre/movie/list")
    suspend fun getGenres(): Genres

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: String): MovieDetail

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(@Path("movie_id") movieId: String): MovieImages

    @GET("search/movie")
    suspend fun search(@Query("query") query: String): SearchResult
}