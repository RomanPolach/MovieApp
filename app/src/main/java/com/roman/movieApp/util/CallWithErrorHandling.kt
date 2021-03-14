package com.roman.movieApp.util

import com.roman.movieApp.model.exception.MovieDbException
import com.roman.movieApp.model.exception.NoInternetException
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class CallWithErrorHandling<T>(
    private val delegate: Call<T>
) : Call<T> by delegate {

    override fun enqueue(callback: Callback<T>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(call, response)
                } else {
                    callback.onFailure(call, mapToDomainException(HttpException(response)))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onFailure(call, mapToDomainException(t))
            }
        })


    }

    fun mapToDomainException(
        remoteException: Throwable
    ): Throwable {
        return when (remoteException) {
            is IOException -> NoInternetException()
            is HttpException -> maptoMovieDbException(remoteException) ?: remoteException
            else -> remoteException
        }
    }

    private fun maptoMovieDbException(remoteException: HttpException): Throwable? {
        return try {
            remoteException.response()?.errorBody()?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(MovieDbException::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }

    override fun clone() = CallWithErrorHandling(delegate.clone())
}