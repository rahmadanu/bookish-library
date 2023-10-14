package com.hepipat.bookish.helper.firebase

import android.content.Intent
import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.hepipat.bookish.BuildConfig
import timber.log.Timber

object GoogleSignInHelper {

    private lateinit var context: FragmentActivity
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private lateinit var signInListener: GoogleSignInListener

    fun initialize(context: FragmentActivity): GoogleSignInHelper {
        this.context = context

        oneTapClient = Identity.getSignInClient(context)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
        return this@GoogleSignInHelper
    }

    fun setListener(listener: GoogleSignInListener): GoogleSignInHelper {
        this.signInListener = listener
        return this@GoogleSignInHelper
    }

    fun showSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(context) {
                try {
                    signInListener.onShowSignInSuccess(it)
                } catch (e: IntentSender.SendIntentException) {
                    Timber.d(e.message)
                }
            }
            .addOnFailureListener(context) {
                signInListener.onShowSignInFailed(it)
            }
    }

    fun performSignIn(data: Intent) {
        val credential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = credential.googleIdToken
        val username = credential.id
        val password = credential.password

        // verify with reactive, if id token is valid, then send username & password
        if (idToken != null) {
            signInListener.onSignInSuccess(idToken, username, password)
        } else {
            signInListener.onSignInFailed("No credential found")
        }
    }

    fun performSignOut() {
        oneTapClient.signOut()
    }
}