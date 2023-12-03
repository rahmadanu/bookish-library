package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.remote.BooksRemoteDataSource
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.mapToBooksUi
import com.hepipat.bookish.helper.api.Result
import com.hepipat.bookish.helper.api.proceed
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val dataSource: BooksRemoteDataSource,
) : BooksRepository {
    override suspend fun getBooksByIsbn(isbnCode: String): Result<BooksUi> {
        if (isbnCode.isEmpty()) return Result.Error(Exception("ISBN code is empty"))

        return proceed {
            dataSource.getBooksByIsbn(isbnCode).mapToBooksUi()
        }
    }
}