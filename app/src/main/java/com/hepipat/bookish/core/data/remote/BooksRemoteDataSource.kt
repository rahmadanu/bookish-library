package com.hepipat.bookish.core.data.remote

import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.data.remote.response.BorrowBooksResponse
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.data.remote.response.ReturnBooksResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface BooksRemoteDataSource {
    suspend fun getBooks(): List<BooksResponse>
    suspend fun getBooksByIsbn(isbnCode: String): BooksResponse
    suspend fun borrowBook(borrow: BorrowRequestBody): BorrowedResponse
    suspend fun returnBook(partFile: MultipartBody.Part, returnAt: RequestBody, borrowId: RequestBody): ReturnBooksResponse
    suspend fun getBorrowBooks(): List<BorrowBooksResponse>
    suspend fun getReturnBooks(): List<ReturnBooksResponse>
    //search/ ! one shot returns flow
}