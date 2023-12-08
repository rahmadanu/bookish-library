package com.hepipat.bookish.helper.signin

interface GoogleSignInListener {

    interface OnSignInSuccessListener {
        fun onSuccess(idToken: String, username: String, password: String?)
    }

    interface OnSignInFailedListener {
        fun onFailed(message: String)
    }
}