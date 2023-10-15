package com.hepipat.bookish.helper.firebase

data class GoogleUser(
    val idToken: String,
    val username: String,
    val password: String?,
)