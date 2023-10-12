package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.testdata.BooksTestData
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.utils.Result
import com.hepipat.bookish.utils.asResult

class TestBooksRepository: BooksRepository {
    override suspend fun getBooksByISBN(isbnCode: String): Result<BooksUi> {
        if (isbnCode.isEmpty()) return Result.Error(Exception(BooksTestData.emptyErrorMessage))

        return if (isbnCode == BooksTestData.booksUi.isbnCode)
            BooksTestData.booksUi.asResult() else BooksTestData.nullBooksUi.asResult()
    }
}