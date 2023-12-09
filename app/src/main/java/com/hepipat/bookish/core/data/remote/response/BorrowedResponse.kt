package com.hepipat.bookish.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class BorrowedResponse(
    @SerializedName("book_id")
    var bookId: Int = 0,
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("deadline_at")
    var deadlineAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("status")
    var status: String = "",
    @SerializedName("updatedAt")
    var updatedAt: String = "",
    @SerializedName("user_id")
    var userId: Int = 0
)