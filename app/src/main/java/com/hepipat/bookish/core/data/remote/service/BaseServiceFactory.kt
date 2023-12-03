package com.hepipat.bookish.core.data.remote.service

import com.hepipat.bookish.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BaseServiceFactory {
    private fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
            .apply {
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                connectTimeout(60, TimeUnit.SECONDS)
            }
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        httpClient.addInterceptor(NetworkInterceptor())
        return httpClient.build()
    }

    private fun provideBooksRetrofit(): BooksApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_BOOKISH_API)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(BooksApiService::class.java)
    }

    fun getBooksApiService(): BooksApiService {
        return provideBooksRetrofit()
    }
}