package com.roman.movieApp.model.repository

import com.roman.movieApp.model.api.Movie
import com.roman.movieApp.model.api.MovieDetail
import com.roman.movieApp.model.api.RequestResult
import com.roman.movieApp.model.retrofit.ApiDescription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
* Repository for movies. Exceptions are captured in custom Call Adapter of Retrofit and converted to our own Exceptions (NoInternetException, etc)
* */
class MovieRepositoryImpl(val apiDescription: ApiDescription) : MovieRepository {
    private val language = "en-US"
    private val year = "2020"
    private val sortBy = "popularity.desc"

    override suspend fun getMoviesPage(page: Int): RequestResult {
        val genres = apiDescription.getGenres()

        val requestResult = apiDescription.getMoviesPage(language, year, sortBy, page)
        requestResult.movies?.forEach {
            it.genres = it.genreIds?.map { id ->
                genres.genres?.find { genre ->
                    genre.id == id
                }?.name ?: ""
            } ?: emptyList()
        }
        return requestResult
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