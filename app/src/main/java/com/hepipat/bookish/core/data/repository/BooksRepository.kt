package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.TitleBooksUi
import com.hepipat.bookish.helper.api.Result

interface BooksRepository {
    suspend fun getBooks(): Result<List<TitleBooksUi>>
    suspend fun getBooksByIsbn(isbnCode: String): Result<BooksUi>
    suspend fun borrowBook(borrow: BorrowRequestBody): Result<BorrowedResponse>
    suspend fun getMyBooks(): Result<List<TitleBooksUi>>
}