package com.hepipat.bookish.feature.returnbook

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.hepipat.bookish.R
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentReturnBinding
import com.hepipat.bookish.helper.error.ErrorCodeHelper
import com.hepipat.bookish.helper.fileprocessing.reduceFileImage
import com.hepipat.bookish.helper.fileprocessing.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ReturnFragment : BaseFragment<FragmentReturnBinding>() {

    private val returnArgs: ReturnFragmentArgs by navArgs()

    private val viewModel: ReturnViewModel by viewModels()

    private lateinit var uri: Uri

    private val REQUEST_IMAGE_SELECT = 100
    private var getFile: File? = null
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentReturnBinding = FragmentReturnBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        binding.apply {
                btnReturn.isEnabled = false
                cvAddImage.setOnClickListener {
                    chooseImageSource()
                }
                btnReturn.setOnClickListener {
                    getFile.let { photoFile -> uploadImage(photoFile) }
            }
        }
    }

    private fun uploadImage(photo: File?) {
        val reducedPhoto = photo?.let { reduceFileImage(it) }

        val fileRequestBody = reducedPhoto?.asRequestBody(FILE_TYPE.toMediaTypeOrNull())

        val partFile = fileRequestBody?.let {
            MultipartBody.Part.createFormData(
                FILE_PARAM, reducedPhoto.name, it
            )
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())

        if (partFile != null) {
            viewModel.returnBook(partFile, currentDateandTime, returnArgs.borrowId)
        }
    }

    private fun chooseImageSource() {
        AlertDialog.Builder(requireActivity())
            .setMessage("Select Picture")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        uri = FileProvider.getUriForFile(
            requireActivity(),
            "${requireActivity().packageName}.provider",
            photoFile
        )
        cameraResult.launch(uri)
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                handleCameraImage(uri)
            }
        }

    private fun handleCameraImage(uri: Uri) {
        val myFile = uriToFile(uri, requireContext())
        binding.apply {
            ivImg.load(uri)
            ivImg.visibility = VISIBLE
            ivAddImg.visibility = GONE
            tvAddImage.visibility = GONE
            btnReturn.isEnabled = true
        }

        getFile = myFile

    }

    private fun openGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_SELECT)
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.returnUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                when (it) {
                    is ReturnBookUiState.Success -> {
                        findNavController().navigate(R.id.action_returnFragment_to_myBooksFragment)
                    }
                    is ReturnBookUiState.Error -> {
                        ErrorCodeHelper.getErrorMessage(requireContext(), it.exception)?.let { message ->
                            showToast(message)
                        }
                    }
                    is ReturnBookUiState.Loading -> {
                        //showToast("Loading...")
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImg: Uri = data.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            binding.apply {
                ivImg.load(selectedImg)
                ivImg.visibility = VISIBLE
                ivAddImg.visibility = GONE
                tvAddImage.visibility = GONE
                btnReturn.isEnabled = true
            }


            getFile = myFile
        }
    }

    companion object {
        private const val FILE_TYPE = "image/jpeg"
        private const val FILE_PARAM = "file"
    }
}