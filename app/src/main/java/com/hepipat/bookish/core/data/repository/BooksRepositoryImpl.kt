package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.remote.BooksRemoteDataSource
import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.TitleBooksUi
import com.hepipat.bookish.core.domain.model.mapToBooksUi
import com.hepipat.bookish.helper.api.Result
import com.hepipat.bookish.helper.api.proceed
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val dataSource: BooksRemoteDataSource,
) : BooksRepository {
    override suspend fun getBooks(): Result<List<TitleBooksUi>> {
        val homeBooks = mutableListOf<TitleBooksUi>()

        return proceed {
            val libraryBooks = dataSource.getBooks().map { it.mapToBooksUi() }
            homeBooks.add(TitleBooksUi("Library Books", libraryBooks))
            homeBooks
        }
    }

    override suspend fun getBooksByIsbn(isbnCode: String): Result<BooksUi> {
        if (isbnCode.isEmpty()) return Result.Error(Exception("ISBN code is empty"))

        return proceed {
            dataSource.getBooksByIsbn(isbnCode).mapToBooksUi()
        }
    }

    override suspend fun borrowBook(borrow: BorrowRequestBody): Result<BorrowedResponse> {
        return proceed {
            dataSource.borrowBook(borrow)
        }
    }

    override suspend fun getMyBooks(): Result<List<TitleBooksUi>> {
        val myBooks = mutableListOf<TitleBooksUi>()

        return proceed {
            val borrowedBooks = dataSource.getBorrowBooks().map { it.book.mapToBooksUi() }
            //val returnedBooks = dataSource.getReturnBooks().map { it.book.mapToBooksUi() }
            val returnedBooks = emptyList<BooksUi>()
            myBooks.add(TitleBooksUi("Borrowed Books", borrowedBooks))
            myBooks.add(TitleBooksUi("Returned Books", returnedBooks))
            myBooks
        }
    }
}