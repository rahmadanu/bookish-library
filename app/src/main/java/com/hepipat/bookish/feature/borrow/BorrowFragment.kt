package com.hepipat.bookish.feature.borrow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hepipat.bookish.R
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
import com.hepipat.bookish.databinding.DialogConfirmBorrowBinding
import com.hepipat.bookish.databinding.DialogSuccessBinding
import com.hepipat.bookish.databinding.FragmentBorrowBinding
import com.hepipat.bookish.helper.error.ErrorCodeHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BorrowFragment : BaseFragment<FragmentBorrowBinding>() {

    private val borrowArgs: BorrowFragmentArgs by navArgs()

    private val viewModel: BorrowViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBorrowBinding = FragmentBorrowBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        binding.apply {
            borrowArgs.booksUi.apply {
                Glide.with(requireContext())
                    .load(image)
                    .into(ivCover)

                tvTitle.text = title
                tvAuthor.text = author
                tvPublisher.text = publisher
                tvReleaseDate.text = releaseDate
                tvDescription.text = description

                btnBorrow.setOnClickListener {
                    confirmBorrowDialog(id)
                }
            }
        }
    }

    private fun confirmBorrowDialog(id: String) {
        val confirmBorrow = DialogConfirmBorrowBinding.inflate(LayoutInflater.from(requireContext()))
        val borrowDialogBuilder = AlertDialog.Builder(requireContext(), R.style.RoundedCornerDialog)
            .setView(confirmBorrow.root)
        borrowDialogBuilder.setCancelable(true)
        val showBorrowDialog = borrowDialogBuilder.show()
        confirmBorrow.btnConfirm.setOnClickListener {
            viewModel.borrowBook(BorrowRequestBody(1, id.toInt(), "2023-12-30"))
            showBorrowDialog.dismiss()
        }
        confirmBorrow.btnDismiss.setOnClickListener {
            showBorrowDialog.cancel()
        }

    }

    override fun initObserver() {
        super.initObserver()
        viewModel.borrowUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                when (it) {
                    is BorrowBookUiState.Success -> {
//                        showToast("Success add")
                        showSuccessDialog()
                    }
                    is BorrowBookUiState.Error -> {
                        ErrorCodeHelper.getErrorMessage(requireContext(), it.exception)?.let { message ->
                            showToast(message)
                        }
                    }
                    is BorrowBookUiState.Loading -> {
                        //showToast("Loading...")
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun showSuccessDialog() {
        val successDialog = DialogSuccessBinding.inflate(LayoutInflater.from(requireContext()))
        val successDialogBuilder = AlertDialog.Builder(requireContext(), R.style.RoundedCornerDialog)
            .setView(successDialog.root)
        successDialogBuilder.setCancelable(true)
        val showSuccessDialog = successDialogBuilder.show()

        Handler(Looper.myLooper()!!).postDelayed({
            showSuccessDialog.dismiss()
        }, 3000)
        findNavController().navigate(R.id.action_borrowFragment_to_homeFragment)
    }
}