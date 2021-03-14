package com.roman.movieApp.ui.main

import app.cash.turbine.test
import com.roman.movieApp.model.api.Movie
import com.roman.movieApp.model.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

/**
 * Test class for MainViewModel
 */
@ExperimentalTime
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Mock
    private lateinit var movieRepository: MovieRepository

    @Test
    fun when_set_empty_string_as_search_no_search_should_be_performed() {
        val viewModel = MainViewModel(movieRepository)
        viewModel.setSearchTerm("")
        runBlocking {
            viewModel.searchResultsFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun when_search_term_is_non_empty_search_should_be_performed() {
        val viewModel = MainViewModel(movieRepository)
        runBlocking {
            val testMovie = Movie(title = "Matrix")
            doReturn(flowOf(listOf(testMovie)))
                .`when`(movieRepository)
                .search("Matrix")
            viewModel.setSearchTerm("Matrix")
            viewModel.searchResultsFlow.test {
                val emitedItem = expectItem()
                assertEquals(emitedItem?.get(0), testMovie)
                assertEquals(emitedItem?.size, 1)
                expectNoEvents()
            }
        }
    }
}