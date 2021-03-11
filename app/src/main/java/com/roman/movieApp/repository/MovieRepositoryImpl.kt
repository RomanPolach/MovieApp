package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
* Repository for movies. Exceptions are captured in custom Call Adapter of Retrofit and converted to our own Exceptions (NoInternetException, etc)
* */
class MovieRepositoryImpl(val apiDescription: ApiDescription) : MovieRepository {
    private val language = "en-US"
    private val year = "2020"
    private val sortBy = "popularity.desc"
    private var lastRequestResult: RequestResult? = null
    private var loadedMovies: List<Movie> = emptyList()

    override suspend fun getMoviesPage(): List<Movie> {
        val genres = apiDescription.getGenres()
        var requestResult: RequestResult? = null
        val movies: List<Movie>

        if (lastRequestResult == null) {
            requestResult = apiDescription.getMoviesPage(language, year, sortBy)
            movies = requestResult.movies ?: emptyList()
        } else if (lastRequestResult != null && lastRequestResult!!.page < lastRequestResult!!.totalPages!!) {
            requestResult = apiDescription.getMoviesPage(language, year, sortBy, (lastRequestResult!!.page + 1).toString())
            movies = loadedMovies + (requestResult.movies ?: emptyList())
        } else {
            movies = loadedMovies
        }

        movies.forEach {
            it.genres = it.genreIds?.map { id ->
                genres.genres?.find { genre ->
                    genre.id == id
                }?.name ?: ""
            } ?: emptyList()
        }
        lastRequestResult = requestResult
        loadedMovies = movies
        return movies
    }

    override suspend fun getMovieDetail(movieId: String): MovieDetail {
        val movie = apiDescription.getMovieDetail(movieId)
        val images = apiDescription.getMovieImages(movieId)
        movie.images = images
        return movie
    }

    override fun search(movie: String): Flow<List<Movie>?> {
        return flow {
            val results = apiDescription.search(movie)
            emit(results.movies)
        }
    }
}