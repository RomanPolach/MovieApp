package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(val apiDescription: ApiDescription) : MovieRepository {
    private val language = "en-US"
    private val year = "2020"
    private val sortBy = "popularity.desc"
    private var lastRequestResult: RequestResult? = null
    private var loadedMovies: List<Result> = emptyList()

    override fun getMoviesPage(): Flow<List<Result>?> {
        return flow {

            val genres = apiDescription.getGenres()
            var requestResult: RequestResult? = null
            val results: List<Result>

            if (lastRequestResult == null) {
                requestResult = apiDescription.getMoviesPage(language, year, sortBy)
                results = requestResult.results ?: emptyList()
            } else if (lastRequestResult != null && lastRequestResult!!.page < lastRequestResult!!.totalPages!!) {
                requestResult = apiDescription.getMoviesPage(language, year, sortBy, (lastRequestResult!!.page + 1).toString())
                results = loadedMovies + (requestResult.results ?: emptyList())
            } else {
                results = loadedMovies
            }

            results.forEach {
                it.genres = it.genreIds?.map { id ->
                    genres.genres?.find { genre ->
                        genre.id == id
                    }?.name ?: ""
                } ?: emptyList()
            }
            lastRequestResult = requestResult
            loadedMovies = results
            emit(results)
        }
    }

    override fun getMovieDetail(movieId: String): Flow<MovieDetail> {
        return flow {
            val movie = apiDescription.getMovieDetail(movieId)
            val images = apiDescription.getMovieImages(movieId)
            movie.images = images
            emit(movie)
        }
    }

    override fun search(movie: String): Flow<List<Result>?> {
        return flow {
            val results = apiDescription.search(movie)
            emit(results.results)
        }
    }
}