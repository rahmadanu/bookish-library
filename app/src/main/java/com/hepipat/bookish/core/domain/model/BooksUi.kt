package com.hepipat.bookish.core.domain.model

import com.hepipat.bookish.core.data.remote.response.BooksResponse

data class BooksUi constructor(
    val id: String,
    val title: String,
    val description: String,
    val isbnCode: String,
    val publishDate: String,
    val author: String,
) {

    constructor(booksResponse: BooksResponse) : this(
        id = booksResponse.items.getOrNull(0)?.id ?: "",
        title = booksResponse.items.getOrNull(0)?.volumeInfo?.title ?: "",
        description = booksResponse.items.getOrNull(0)?.volumeInfo?.description ?: "",
        isbnCode = booksResponse.items.getOrNull(0)?.volumeInfo
            ?.industryIdentifiers?.firstOrNull { it.identifier.length == 13 }?.identifier ?: "",
        publishDate = booksResponse.items.getOrNull(0)?.volumeInfo?.publishedDate ?: "",
        author = booksResponse.items.getOrNull(0)?.volumeInfo?.publishedDate ?: "",
    )
}

fun BooksResponse.mapToBooksUi() = BooksUi(this)