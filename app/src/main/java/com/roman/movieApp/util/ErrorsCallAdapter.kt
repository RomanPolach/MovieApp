package com.roman.movieApp.util

import retrofit2.Call
import retrofit2.CallAdapter

class ErrorsCallAdapter<T>(
    private val delegateAdapter: CallAdapter<T, Call<*>>
) : CallAdapter<T, Call<*>> by delegateAdapter {

    override fun adapt(call: Call<T>): Call<*> {
        return delegateAdapter.adapt(CallWithErrorHandling(call))
    }
}