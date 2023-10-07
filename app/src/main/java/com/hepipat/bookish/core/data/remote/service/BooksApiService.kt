package com.hepipat.bookish.core.data.remote.service

import com.hepipat.bookish.core.data.remote.response.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String
    ): BooksResponse
}