package com.roman.movieApp.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genres(
    @Json(name = "genres")
    val genres: List<Genre>? = listOf()
)

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "id")
    val id: Int? = 0,
    @Json(name = "name")
    val name: String? = ""
)