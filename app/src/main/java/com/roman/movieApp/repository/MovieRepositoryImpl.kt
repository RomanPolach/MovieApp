package com.roman.movieApp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(val apiDescription: ApiDescription) : MovieRepository {
    private val apiKEY = "cd1ee4ab83bf0c066f60254511f8c3f4"
    private val language = "en-US"

    override suspend fun getMovies(): Flow<List<Result>?> {
        return flow {
            val genres = apiDescription.getGenres(apiKEY)
            val movies = apiDescription.getMovies(apiKEY, language, "2020").results ?: emptyList()

            movies?.forEach {
                it.genres = it.genreIds?.map { id ->
                    genres.genres?.find {
                        it.id == id
                    }?.name ?: ""
                } ?: emptyList()
            }
            emit(movies)
        }
    }

}