package com.roman.movieApp.util

import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.roman.movieApp.repository.MovieDbException
import com.roman.movieApp.repository.NoInternetException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Easily add models to an EpoxyRecyclerView, the same way you would in a buildModels method of EpoxyController. */
fun EpoxyRecyclerView.withModels(buildModelsCallback: EpoxyController.() -> Unit) {
    setControllerAndBuildModels(object : EpoxyController() {
        override fun buildModels() {
            buildModelsCallback()
        }
    })
}

// Method for launching coroutines, which automaticaly sets loading, loaded or error states to mutablelivedata
fun <T> CoroutineScope.launchRequestWithState(request: suspend () -> T, mutableLiveData: MutableLiveData<State<T>>) {
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        mutableLiveData.setError(exception)
    }
    launch(exceptionHandler) {
        mutableLiveData.setLoading()
        mutableLiveData.setLoaded(request())
    }
}

fun Fragment.handleError(error: Throwable) {
    when (error) {
        is NoInternetException -> showToast("Internet is not working")
        is MovieDbException -> showToast(error.status_message)
    }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun <T> MutableLiveData<State<T>>.setLoading() {
    postValue(State.Loading)
}

fun <T> MutableLiveData<State<T>>.setLoaded(data: T) {
    postValue(State.Loaded(data))
}

fun <T> MutableLiveData<State<T>>.setError(exception: Throwable) {
    postValue(State.Error(exception))
}