package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.request.ReturnRequestBody
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.data.remote.response.ReturnBooksResponse
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.TitleBooksUi
import com.hepipat.bookish.helper.api.Result
import okhttp3.MultipartBody

interface BooksRepository {
    suspend fun getBooks(): Result<List<TitleBooksUi>>
    suspend fun getBooksByIsbn(isbnCode: String): Result<BooksUi>
    suspend fun borrowBook(borrow: BorrowRequestBody): Result<BorrowedResponse>
    suspend fun returnBook(partFile: MultipartBody.Part, returnedAt: String, borrowId: String): Result<ReturnBooksResponse>
    suspend fun getMyBooks(): Result<List<TitleBooksUi>>
}