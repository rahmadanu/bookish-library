package com.hepipat.bookish.core.data.remote

import com.hepipat.bookish.core.data.remote.response.BooksResponse
import kotlinx.coroutines.flow.Flow

interface BooksRemoteDataSource {
    suspend fun getBooksByISBN(isbnCode: String): BooksResponse
    //search/ ! one shot returns flow
}