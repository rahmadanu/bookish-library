package com.hepipat.bookish.core.data.remote

import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.data.remote.response.BorrowBooksResponse
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.data.remote.response.ReturnBooksResponse
import com.hepipat.bookish.core.data.remote.service.BooksApiService
import javax.inject.Inject

class BooksRemoteDataSourceImpl @Inject constructor(
    private val apiService: BooksApiService,
) : BooksRemoteDataSource {
    override suspend fun getBooks(): List<BooksResponse> {
        return apiService.getBooks()
    }

    override suspend fun getBooksByIsbn(isbnCode: String): BooksResponse {
        return apiService.getBookByIsbn(isbnCode)
    }

    override suspend fun borrowBook(borrow: BorrowRequestBody): BorrowedResponse {
        return apiService.borrowBook(borrow)
    }

    override suspend fun getBorrowBooks(): List<BorrowBooksResponse> {
        return apiService.getBorrowBooks()
    }

    override suspend fun getReturnBooks(): List<ReturnBooksResponse> {
        return apiService.getReturnBooks()
    }
}