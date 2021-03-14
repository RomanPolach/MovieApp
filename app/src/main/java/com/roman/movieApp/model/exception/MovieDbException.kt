package com.roman.movieApp.model.exception

data class MovieDbException(val status_message: String, val status_code: String) : Exception()