package com.hepipat.bookish.core.data.remote.service

import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.data.remote.response.BorrowBooksResponse
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.data.remote.response.ReturnBooksResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BooksApiService {

    @GET("books/isbn/{isbn_code}")
    suspend fun getBookByIsbn(
        @Path("isbn_code") isbn: String
    ): BooksResponse

    @POST("borrow")
    suspend fun borrowBook(
        @Body borrow: BorrowRequestBody
    ): BorrowedResponse

    @GET("borrow")
    suspend fun getBorrowBooks(): List<BorrowBooksResponse>

    @GET("return")
    suspend fun getReturnBooks(): List<ReturnBooksResponse>
}