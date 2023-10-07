package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.remote.BooksRemoteDataSource
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.mapToBooksUi
import com.hepipat.bookish.utils.Result
import com.hepipat.bookish.utils.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val dataSource: BooksRemoteDataSource
): BooksRepository {
    override suspend fun getBooksByISBN(isbnCode: String): Result<BooksUi> {
        return dataSource.getBooksByISBN(isbnCode)
            .mapToBooksUi()
            .asResult()
    }
}