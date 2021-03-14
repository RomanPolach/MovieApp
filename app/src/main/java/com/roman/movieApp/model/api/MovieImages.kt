package com.roman.movieApp.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MovieImages(
    @Json(name = "backdrops")
    val backdrops: List<Backdrop>? = listOf(),
    @Json(name = "id")
    val id: Int? = 0,
    @Json(name = "posters")
    val posters: List<Poster>? = listOf()
)

@JsonClass(generateAdapter = true)
data class Backdrop(
    @Json(name = "aspect_ratio")
    val aspectRatio: Double? = 0.0,
    @Json(name = "file_path")
    val filePath: String? = "",
    @Json(name = "height")
    val height: Int? = 0,
    @Json(name = "iso_639_1")
    val iso6391: Any? = Any(),
    @Json(name = "vote_average")
    val voteAverage: Double? = 0.0,
    @Json(name = "vote_count")
    val voteCount: Int? = 0,
    @Json(name = "width")
    val width: Int? = 0
)

@JsonClass(generateAdapter = true)
data class Poster(
    @Json(name = "aspect_ratio")
    val aspectRatio: Double? = 0.0,
    @Json(name = "file_path")
    val filePath: String? = "",
    @Json(name = "height")
    val height: Int? = 0,
    @Json(name = "iso_639_1")
    val iso6391: String? = "",
    @Json(name = "vote_average")
    val voteAverage: Double? = 0.0,
    @Json(name = "vote_count")
    val voteCount: Int? = 0,
    @Json(name = "width")
    val width: Int? = 0
)