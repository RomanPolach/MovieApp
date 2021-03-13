package com.roman.movieApp.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestResult(
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "results")
    var movies: List<Movie>? = listOf(),
    @Json(name = "total_pages")
    val totalPages: Int? = 0,
    @Json(name = "total_results")
    val totalResults: Int? = 0
)