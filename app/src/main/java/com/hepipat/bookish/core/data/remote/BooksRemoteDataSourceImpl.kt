package com.hepipat.bookish.core.data.remote

import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.data.remote.service.BooksApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

class BooksRemoteDataSourceImpl @Inject constructor(
    private val apiService: BooksApiService
) : BooksRemoteDataSource {
    override suspend fun getBooksByISBN(isbnCode: String): BooksResponse {
        return apiService.getBooks("isbn:$isbnCode")
    }
}