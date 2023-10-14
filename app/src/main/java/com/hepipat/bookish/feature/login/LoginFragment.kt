package com.hepipat.bookish.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentLoginBinding
import com.hepipat.bookish.helper.firebase.GoogleSignInHelper
import com.hepipat.bookish.helper.firebase.GoogleSignInListener
import timber.log.Timber

class LoginFragment : BaseFragment<FragmentLoginBinding>(), GoogleSignInListener {

    private val signInResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                it.data?.let { intent -> GoogleSignInHelper.performSignIn(intent) }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        showToast("Sign-in dialog was closed")
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        showToast("Sign-in received network error")
                    }

                    else -> {
                        showToast("Sign-in failed: ${e.localizedMessage}")
                    }
                }
            }
        }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {}

    override fun initClickListener() {
        super.initClickListener()

        binding.btnSignIn.setOnClickListener {
            showSignInDialog()
        }
    }

    private fun showSignInDialog() {
        GoogleSignInHelper.initialize(requireActivity())
            .setListener(this)
            .showSignIn()
    }

    override fun onShowSignInSuccess(result: BeginSignInResult) {
        Timber.d("calling success")
        signInResult.launch(IntentSenderRequest.Builder(result.pendingIntent).build())
    }

    override fun onShowSignInFailed(e: Exception) {
        Timber.d("calling failed $e")
    }

    override fun onSignInSuccess(idToken: String, username: String, password: String?) {
        binding.btnSignIn.text = username
        binding.btnSignIn.isEnabled = false
        // send to backend
    }

    override fun onSignInFailed(message: String) {
        showToast(message)
    }
}