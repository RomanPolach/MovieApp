package com.roman.movieApp.di

import com.roman.movieApp.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { MainViewModel(get()) }
//
//    viewModel { AddRecipeViewModel(repository = get()) }
//
//    viewModel { (recipeId: String) -> RecipeDetailViewModel(get(), recipeId) }


}