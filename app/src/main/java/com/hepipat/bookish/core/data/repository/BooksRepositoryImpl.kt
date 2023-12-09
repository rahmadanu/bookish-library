package com.hepipat.bookish.core.data.repository

import com.hepipat.bookish.core.data.remote.BooksRemoteDataSource
import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.core.data.remote.response.BorrowedResponse
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.MyBooksUi
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

    override suspend fun borrowBook(borrow: BorrowRequestBody): Result<BorrowedResponse> {
        return proceed {
            dataSource.borrowBook(borrow)
        }
    }

    override suspend fun getMyBooks(): Result<List<MyBooksUi>> {
        val myBooks = mutableListOf<MyBooksUi>()

        return proceed {
            val borrowedBooks = dataSource.getBorrowBooks().map { it.book.mapToBooksUi() }
            //val returnedBooks = dataSource.getReturnBooks().map { it.book.mapToBooksUi() }
            val returnedBooks = emptyList<BooksUi>()
            myBooks.add(MyBooksUi("Borrowed Books", borrowedBooks))
            myBooks.add(MyBooksUi("Returned Books", returnedBooks))
            /*myBooks.add(MyBooksUi("Returned Books", listOf(
                BooksUi(
                    id = "1",
                    title = "Physics for dummy",
                    publisher = "Physics 2",
                    description = "Physics 2 description",
                    author = "Dandelion",
                    releaseDate = "2023-04-20",
                    image = "https://res.cloudinary.com/dv1ub4ivc/image/upload/v1701572554/bookish/ibjkwueoelq8nlin5sdj.jpg"
                ),
                BooksUi(
                    id = "1",
                    title = "Physics for dummy",
                    publisher = "Physics 2",
                    description = "Physics 2 description",
                    author = "Dandelion",
                    releaseDate = "2023-04-20",
                    image = "https://res.cloudinary.com/dv1ub4ivc/image/upload/v1701572554/bookish/ibjkwueoelq8nlin5sdj.jpg"
                ),
                BooksUi(
                    id = "1",
                    title = "Physics for dummy",
                    publisher = "Physics 2",
                    description = "Physics 2 description",
                    author = "Dandelion",
                    releaseDate = "2023-04-20",
                    image = "https://res.cloudinary.com/dv1ub4ivc/image/upload/v1701572554/bookish/ibjkwueoelq8nlin5sdj.jpg"
                ),
                BooksUi(
                    id = "1",
                    title = "Physics for dummy",
                    publisher = "Physics 2",
                    description = "Physics 2 description",
                    author = "Dandelion",
                    releaseDate = "2023-04-20",
                    image = "https://res.cloudinary.com/dv1ub4ivc/image/upload/v1701572554/bookish/ibjkwueoelq8nlin5sdj.jpg"
                ),
                BooksUi(
                    id = "1",
                    title = "Physics for dummy",
                    publisher = "Physics 2",
                    description = "Physics 2 description",
                    author = "Dandelion",
                    releaseDate = "2023-04-20",
                    image = "https://res.cloudinary.com/dv1ub4ivc/image/upload/v1701572554/bookish/ibjkwueoelq8nlin5sdj.jpg"
                ),
                BooksUi(
                    id = "1",
                    title = "Physics for dummy",
                    publisher = "Physics 2",
                    description = "Physics 2 description",
                    author = "Dandelion",
                    releaseDate = "2023-04-20",
                    image = "https://res.cloudinary.com/dv1ub4ivc/image/upload/v1701572554/bookish/ibjkwueoelq8nlin5sdj.jpg"
                ),
                BooksUi(
                    id = "1",
                    title = "Physics for dummy",
                    publisher = "Physics 2",
                    description = "Physics 2 description",
                    author = "Dandelion",
                    releaseDate = "2023-04-20",
                    image = "https://res.cloudinary.com/dv1ub4ivc/image/upload/v1701572554/bookish/ibjkwueoelq8nlin5sdj.jpg"
                ),
            )))*/
            myBooks
        }
    }
}