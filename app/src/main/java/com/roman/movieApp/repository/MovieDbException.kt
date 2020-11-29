package com.roman.movieApp.repository

data class MovieDbException(val status_message: String, val status_code: String) : Exception()