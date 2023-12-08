package com.hepipat.bookish.core.data.remote

import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.data.remote.response.BorrowBooksResponse
import com.hepipat.bookish.core.data.remote.response.ReturnBooksResponse
import com.hepipat.bookish.core.data.remote.service.BooksApiService
import javax.inject.Inject

class BooksRemoteDataSourceImpl @Inject constructor(
    private val apiService: BooksApiService,
) : BooksRemoteDataSource {
    override suspend fun getBooksByIsbn(isbnCode: String): BooksResponse {
        return apiService.getBookByIsbn(isbnCode)
    }

    override suspend fun getBorrowBooks(): List<BorrowBooksResponse> {
        return apiService.getBorrowBooks()
    }

    override suspend fun getReturnBooks(): List<ReturnBooksResponse> {
        return apiService.getReturnBooks()
    }
}