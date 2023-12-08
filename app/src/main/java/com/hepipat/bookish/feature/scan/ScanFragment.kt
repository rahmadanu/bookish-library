package com.hepipat.bookish.feature.scan

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentScanBinding
import com.hepipat.bookish.helper.error.ErrorCodeHelper
import com.hepipat.bookish.helper.permission.PermissionCore
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

    private val settingsPermissionResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            checkPermissions()
        }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentScanBinding = FragmentScanBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
    }
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
                        if (lastScannedBook != "") {
                            val action =
                                ScanFragmentDirections.actionScanFragmentToBorrowFragment(it.booksUi)
                            findNavController().navigate(action)
                            lastScannedBook = ""
                        }
                    }
                    is ScanBooksUiState.NotFound -> {
                        showToast("Book not found")
                    }
                    is ScanBooksUiState.Failed -> {}
                    is ScanBooksUiState.Error -> {
                        ErrorCodeHelper.getErrorMessage(requireContext(), it.exception)?.let { message ->
                            showToast(message)
                        }
                    }
                    is ScanBooksUiState.Loading -> {
                        //showToast("Loading...")
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

    private fun checkPermissions() {
        PermissionCore(requireActivity())
            .permissions(listOf(Manifest.permission.CAMERA))
            .onAccepted {}
            .onDenied {}
            .onForeverDenied {
                it.forEach { permission ->
                    if (permission == Manifest.permission.CAMERA) {
                        showMandatoryPermissionDialog()
                    }
                }
            }
            .ask()
    }

    private fun showMandatoryPermissionDialog() {
        showDialogConfirmation(
            title = "Need your permission",
            message = "Scan feature needs your permission",
            confirmCallback = {
                val appInfoIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                appInfoIntent.data = uri
                settingsPermissionResult.launch(appInfoIntent)
            },
            cancelCallback = {
                parentFragmentManager.popBackStack()
            },
            null,
            false
        )
    }
}