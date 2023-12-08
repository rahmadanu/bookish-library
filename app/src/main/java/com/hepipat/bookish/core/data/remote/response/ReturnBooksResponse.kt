package com.hepipat.bookish.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ReturnBooksResponse(
    @SerializedName("book_id")
    var bookId: Int = 0,
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("charge")
    var charge: String = "",
    @SerializedName("late_time")
    var lateTime: String = "",
    @SerializedName("returned_at")
    var returnedAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("status")
    var status: String = "",
    @SerializedName("updatedAt")
    var updatedAt: String = "",
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("borrow_id")
    var borrowId: Int,
)
