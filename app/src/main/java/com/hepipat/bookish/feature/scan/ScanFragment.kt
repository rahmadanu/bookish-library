package com.hepipat.bookish.feature.scan

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentScanBinding
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ScanFragment : BaseFragment<FragmentScanBinding>() {

    private val scanViewModel: ScanViewModel by viewModels()

    private var lastScannedBook = ""

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastScannedBook) {
                // Prevent duplicate scans
                return
            }
            lastScannedBook = result.text
            scanViewModel.scanBooks(lastScannedBook)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentScanBinding = FragmentScanBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
        initScanner()
    }

    private fun initScanner() {
        val formats: Collection<BarcodeFormat> = listOf(BarcodeFormat.EAN_13)

        binding.barcodeScanner.apply {
            barcodeView.decoderFactory = DefaultDecoderFactory(formats)
            decodeContinuous(callback)
        }
    }

    override fun initObserver() {
        super.initObserver()
        scanViewModel.booksUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                when (it) {
                    is ScanBooksUiState.Scanned -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Book scanned")
                            .setMessage("Book Title: ${it.booksUi.title}")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                    is ScanBooksUiState.NotFound -> {
                        showToast("Book not found")
                    }
                    is ScanBooksUiState.Failed -> {}
                    is ScanBooksUiState.Error -> {
                        showToast("Error ${it.message}")
                    }
                    is ScanBooksUiState.Loading -> {
                        showToast("Loading...")
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }

}