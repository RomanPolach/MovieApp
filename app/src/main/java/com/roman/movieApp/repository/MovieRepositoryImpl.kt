package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(val apiDescription: ApiDescription) : MovieRepository {
    private val language = "en-US"
    private val year = "2020"
    private val sortBy = "popularity.desc"

    override suspend fun getMovies(): Flow<List<Result>?> {
        return flow {
            val genres = apiDescription.getGenres()
            val movies = apiDescription.getMovies(language, year, sortBy).results ?: emptyList()

            movies.forEach {
                it.genres = it.genreIds?.map { id ->
                    genres.genres?.find { genre ->
                        genre.id == id
                    }?.name ?: ""
                } ?: emptyList()
            }
            emit(movies)
        }
    }

    override suspend fun getMovieDetail(movieId: String): Flow<MovieDetail> {
        return flow {
            val movie = apiDescription.getMovieDetail(movieId)
            val images = apiDescription.getMovieImages(movieId)
            movie.images = images
            emit(movie)
        }
    }

    override suspend fun search(movie: String): Flow<List<Result>?> {
        return flow {
            val results = apiDescription.search(movie)
            emit(results.results)
        }
    }
}