package com.hepipat.bookish.helper.firebase

import com.google.android.gms.auth.api.identity.BeginSignInResult

interface GoogleSignInListener {
    fun onShowSignInSuccess(result: BeginSignInResult)
    fun onShowSignInFailed(e: Exception)
    fun onSignInSuccess(idToken: String, username: String, password: String?)
    fun onSignInFailed(message: String)
}