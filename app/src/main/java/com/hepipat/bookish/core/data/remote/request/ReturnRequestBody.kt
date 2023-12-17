package com.hepipat.bookish.core.data.remote.request

import com.google.gson.annotations.SerializedName

data class ReturnRequestBody(
    @SerializedName("returned_at")
    var returnedAt: String = "",
    @SerializedName("borrow_id")
    var borrowId: Int = 0
)
