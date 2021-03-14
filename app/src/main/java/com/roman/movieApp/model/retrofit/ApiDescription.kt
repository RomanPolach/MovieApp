package com.roman.movieApp.model.retrofit

import com.roman.movieApp.model.api.Genres
import com.roman.movieApp.model.api.MovieDetail
import com.roman.movieApp.model.api.MovieImages
import com.roman.movieApp.model.api.RequestResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiDescription {
    // ApiKey for requests is inserted to queries automaticaly through interceptor in RemoteModule
    @GET("discover/movie?")
    suspend fun getMoviesPage(
        @Query("language") language: String,
        @Query("primary_release_year") year: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int? = null
    ): RequestResult

    @GET("genre/movie/list")
    suspend fun getGenres(): Genres

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: String): MovieDetail

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(@Path("movie_id") movieId: String): MovieImages

    @GET("search/movie")
    suspend fun search(@Query("query") query: String): RequestResult
}