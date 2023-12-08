package com.hepipat.bookish.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class BooksResponse(
    @SerializedName("author")
    var author: String? = "",
    @SerializedName("available")
    var available: Boolean? = false,
    @SerializedName("createdAt")
    var createdAt: String? = "",
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("image")
    var image: String? = "",
    @SerializedName("isbn")
    var isbn: String? = "",
    @SerializedName("publisher")
    var publisher: String? = "",
    @SerializedName("releasedate")
    var releasedate: String? = "",
    @SerializedName("stock")
    var stock: Int? = 0,
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("updatedAt")
    var updatedAt: String? = ""
)