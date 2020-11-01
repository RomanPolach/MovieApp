package com.roman.movieApp.di

import com.roman.movieApp.ui.main.MainViewModel
import com.roman.movieApp.ui.main.detail.MovieDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { MainViewModel(get()) }

    viewModel { (movieId: String) -> MovieDetailViewModel(get(), movieId) }
}