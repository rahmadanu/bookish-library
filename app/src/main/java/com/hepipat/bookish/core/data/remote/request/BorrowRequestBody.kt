package com.hepipat.bookish.core.data.remote.request

import com.google.gson.annotations.SerializedName

data class BorrowRequestBody(
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("book_id")
    var bookId: Int = 0,
    @SerializedName("deadline_at")
    var deadlineAt: String = "",
)
