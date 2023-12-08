package com.hepipat.bookish.helper.signin

import android.content.Intent
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.hepipat.bookish.BuildConfig

class GoogleSignIn(
    context: Fragment,
    private val successListener: GoogleSignInListener.OnSignInSuccessListener?,
    private val failedListener: GoogleSignInListener.OnSignInFailedListener?,
    private val oneTapClient: SignInClient,
    private val signInRequest: BeginSignInRequest,
) {

    class Builder(private val context: Fragment) {

        private var successListener: GoogleSignInListener.OnSignInSuccessListener? = null
        private var failedListener: GoogleSignInListener.OnSignInFailedListener? = null

        fun setOnSuccessListener(callback: (GoogleUser) -> Unit): Builder {
            this.successListener = object : GoogleSignInListener.OnSignInSuccessListener {
                override fun onSuccess(idToken: String, username: String, password: String?) {
                    callback(GoogleUser(idToken, username, password))
                }
            }
            return this@Builder
        }

        fun setOnFailedListener(callback: (String) -> Unit): Builder {
            this.failedListener = object : GoogleSignInListener.OnSignInFailedListener {
                override fun onFailed(message: String) {
                    callback(message)
                }
            }
            return this@Builder
        }

        fun build(): GoogleSignIn {
            val oneTapClient = createSignInClient()
            val signInRequest = createSignInRequest()

            return GoogleSignIn(
                context,
                successListener,
                failedListener,
                oneTapClient,
                signInRequest
            )
        }

        private fun createSignInClient(): SignInClient =
            Identity.getSignInClient(context.requireActivity())

        private fun createSignInRequest(): BeginSignInRequest =
            BeginSignInRequest.builder()
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
    }

    fun showDialog() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener {
                try {
                    signInResult.launch(IntentSenderRequest.Builder(it.pendingIntent).build())
                } catch (e: IntentSender.SendIntentException) {
                    e.localizedMessage?.let { message -> failedListener?.onFailed(message) }
                }
            }
            .addOnFailureListener {
                it.localizedMessage?.let { message -> failedListener?.onFailed(message) }
            }
    }

    private val signInResult =
        context.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                it.data?.let { intent -> signIn(intent) }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        failedListener?.onFailed("Sign-in was cancelled")
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        failedListener?.onFailed("Sign-in received network error")
                    }

                    else -> {
                        failedListener?.onFailed("Sign-in failed: ${e.localizedMessage}")
                    }
                }
            }
        }

    private fun signIn(data: Intent) {
        val credential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = credential.googleIdToken
        val username = credential.id
        val password = credential.password

        if (idToken != null) {
            successListener?.onSuccess(idToken, username, password)
        } else {
            failedListener?.onFailed("No credential found")
        }
    }

    fun signOut() {
        oneTapClient.signOut()
    }
}