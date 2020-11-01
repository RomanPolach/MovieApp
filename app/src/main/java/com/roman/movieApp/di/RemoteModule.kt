package com.roman.movieApp.di

import com.roman.movieApp.repository.ApiDescription
import com.roman.movieApp.util.apiKEY
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Module for Rest Service.
 */
val remoteModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url = originalHttpUrl.newBuilder().addQueryParameter("api_key", apiKEY).build()
                request.url(url)
                val response = chain.proceed(request.build())
                return@addInterceptor response
            }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .build()
                )
            )
            .client(get())
            .build()
            .create(ApiDescription::class.java)
    }
}