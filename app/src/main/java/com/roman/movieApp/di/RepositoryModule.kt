package com.roman.movieApp.di

import com.roman.movieApp.repository.MovieRepository
import com.roman.movieApp.repository.MovieRepositoryImpl
import org.koin.dsl.module


/**
 * Module for providing repositories.
 */
val repositoryModule = module {

    single<MovieRepository> { MovieRepositoryImpl(get()) }
}