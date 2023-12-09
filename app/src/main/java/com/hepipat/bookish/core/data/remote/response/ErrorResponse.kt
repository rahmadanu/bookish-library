package com.hepipat.bookish.core.data.remote.response


import com.google.gson.annotations.SerializedName
import java.lang.RuntimeException

data class ErrorResponse(
    @SerializedName("status_code")
    var statusCode: Long? = null,
    @SerializedName("message")
    var messages: String? = null,
    @SerializedName("error")
    var error: String? = null,
): RuntimeException()