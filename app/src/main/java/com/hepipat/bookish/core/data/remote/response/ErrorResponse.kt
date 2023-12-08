package com.hepipat.bookish.core.data.remote.response


import com.google.gson.annotations.SerializedName
import java.lang.RuntimeException

data class ErrorResponse(
    @SerializedName("status_code")
    private var statusCode: Long? = null,
    @SerializedName("message")
    private var messages: String? = null,
    @SerializedName("error")
    private var error: String? = null,
): RuntimeException()