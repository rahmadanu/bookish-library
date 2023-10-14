package com.hepipat.bookish.core.data.remote

import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.data.remote.service.BooksApiService
import javax.inject.Inject

class BooksRemoteDataSourceImpl @Inject constructor(
    private val apiService: BooksApiService,
) : BooksRemoteDataSource {
    override suspend fun getBooksByISBN(isbnCode: String): BooksResponse {
        return apiService.getBooks("isbn:$isbnCode")
    }
}