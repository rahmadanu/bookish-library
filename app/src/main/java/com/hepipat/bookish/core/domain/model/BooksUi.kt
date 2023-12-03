package com.hepipat.bookish.core.domain.model

import android.os.Parcelable
import com.hepipat.bookish.core.data.remote.response.BooksResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class BooksUi constructor(
    val id: String,
    val title: String,
    val publisher: String,
    val image: String,
    val description: String,
    val author: String,
    val releaseDate: String,
): Parcelable {

    /*constructor(booksResponse: BooksResponse) : this(
        id = booksResponse.items.getOrNull(0)?.id ?: "",
        title = booksResponse.items.getOrNull(0)?.volumeInfo?.title ?: "",
        description = booksResponse.items.getOrNull(0)?.volumeInfo?.description ?: "",
        isbnCode = booksResponse.items.getOrNull(0)?.volumeInfo
            ?.industryIdentifiers?.firstOrNull { it.identifier.length == 13 }?.identifier ?: "",
        publishDate = booksResponse.items.getOrNull(0)?.volumeInfo?.publishedDate ?: "",
        author = booksResponse.items.getOrNull(0)?.volumeInfo?.publishedDate ?: "",
    )*/

    constructor(book: BooksResponse) : this(
        id = book.id.toString(),
        title = book.title ?: "",
        publisher = book.publisher ?: "",
        image = book.image ?: "",
        description = book.description ?: "",
        author = book.author ?: "",
        releaseDate = book.releasedate ?: "",
    )
}

//fun BooksResponse.mapToBooksUi() = BooksUi(this)
fun BooksResponse.mapToBooksUi() = BooksUi(this)