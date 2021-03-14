package com.roman.movieApp.ui.main.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.roman.movieApp.model.api.Movie
import com.roman.movieApp.model.repository.MovieRepository

class MoviePagingSource(
    val movieRepository: MovieRepository
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val nextPage = params.key ?: 1
            val response = movieRepository.getMoviesPage(nextPage)

            return LoadResult.Page(
                data = response.movies ?: emptyList(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.page == response.totalPages) null else response.page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}