package com.hepipat.bookish.core.data.remote

import com.hepipat.bookish.core.data.remote.response.BooksResponse

interface BooksRemoteDataSource {
    suspend fun getBooksByIsbn(isbnCode: String): BooksResponse
    //search/ ! one shot returns flow
}