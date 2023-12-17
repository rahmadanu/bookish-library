package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.remote.BooksRemoteDataSource
import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.data.remote.response.ReturnBooksResponse
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.TitleBooksUi
import com.hepipat.bookish.core.domain.model.mapToBooksUi
import com.hepipat.bookish.helper.api.Result
import com.hepipat.bookish.helper.api.proceed
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    override suspend fun returnBook(
        partFile: MultipartBody.Part,
        returnedAt: String,
        borrowId: String
    ): Result<ReturnBooksResponse> {
        val returnDate = returnedAt.toRequestBody("text/plain".toMediaType())
        val borrow = borrowId.toRequestBody("text/plain".toMediaType())

        return proceed {
            dataSource.returnBook(partFile, returnDate, borrow)
        }
    }

    override suspend fun getMyBooks(): Result<List<TitleBooksUi>> {
        val myBooks = mutableListOf<TitleBooksUi>()

        return proceed {
            val borrowedBooks = dataSource.getBorrowBooks().map {
                it.book.mapToBooksUi().also {
                    book ->
                    book.borrowed = true
                    book.borrowId = it.id.toString()
                }
            }
            val returnedBooks = dataSource.getReturnBooks().map {
                BooksUi(it.proof)
            }
            myBooks.add(TitleBooksUi("Borrowed Books", borrowedBooks))
            myBooks.add(TitleBooksUi("Returned Books", returnedBooks))
            myBooks
        }
    }
}