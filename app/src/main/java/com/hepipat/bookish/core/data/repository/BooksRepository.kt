package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.helper.api.Result

interface BooksRepository {
    suspend fun getBooksByIsbn(isbnCode: String): Result<BooksUi>
}