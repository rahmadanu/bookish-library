package com.hepipat.bookish.core.domain.model

import android.os.Parcelable
import com.hepipat.bookish.core.data.remote.response.BooksResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class BooksUi(
    val id: String = "",
    val title: String = "",
    val publisher: String = "",
    val image: String = "",
    val description: String = "",
    val author: String = "",
    val releaseDate: String = "",
    var borrowed: Boolean = false,
    var borrowId: String = "",
    var proof: String = "",
): Parcelable {

    constructor(book: BooksResponse) : this(
        id = book.id.toString(),
        title = book.title ?: "",
        publisher = book.publisher ?: "",
        image = book.image ?: "",
        description = book.description ?: "",
        author = book.author ?: "",
        releaseDate = book.releasedate ?: "",
    )

    constructor(image: String) : this(
        proof = image
    )
}

fun BooksResponse.mapToBooksUi() = BooksUi(this)