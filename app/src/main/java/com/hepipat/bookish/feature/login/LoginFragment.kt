package com.hepipat.bookish.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hepipat.bookish.R
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentLoginBinding
import com.hepipat.bookish.helper.signin.GoogleSignIn

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private var signIn: GoogleSignIn? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        signIn = GoogleSignIn.Builder(this@LoginFragment)
            .setOnSuccessListener {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
            .setOnFailedListener {
                showToast(it)
            }
            .build()
    }

    override fun initClickListener() {
        super.initClickListener()

        binding.btnSignIn.setOnClickListener {
            showSignInDialog()
        }
    }

    private fun showSignInDialog() {
        signIn?.showDialog()
    }
}