package com.hepipat.bookish.core.data.datasource

import com.hepipat.bookish.core.data.remote.BooksRemoteDataSource
import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.data.testdata.BooksTestData

class FakeBooksRemoteDataSource: BooksRemoteDataSource {
    override suspend fun getBooksByISBN(isbnCode: String): BooksResponse {
        val bookIsbnFromApi = BooksTestData.booksResponse.items.filter {
            it.volumeInfo.industryIdentifiers.contains(
                BooksResponse.Item.VolumeInfo.IndustryIdentifier(
                    isbnCode,
                    "ISBN-13"
                )
            )
        }
        return if (bookIsbnFromApi.isNotEmpty()) BooksTestData.booksResponse else BooksTestData.emptyBooksResponse
    }
}