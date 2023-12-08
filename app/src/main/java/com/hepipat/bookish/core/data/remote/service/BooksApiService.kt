package com.hepipat.bookish.core.data.remote.service

import com.hepipat.bookish.core.data.remote.response.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {

    @GET("books/isbn/{isbn_code}")
    suspend fun getBookByIsbn(
        @Path("isbn_code") isbn: String
    ): BooksResponse
}