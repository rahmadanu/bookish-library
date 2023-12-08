package com.hepipat.bookish.helper.signin

data class GoogleUser(
    val idToken: String,
    val username: String,
    val password: String?,
)