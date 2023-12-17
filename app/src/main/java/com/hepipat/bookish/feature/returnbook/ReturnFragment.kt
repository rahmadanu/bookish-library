package com.hepipat.bookish.feature.returnbook

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import coil.load
import com.hepipat.bookish.R
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.core.data.remote.request.ReturnRequestBody
import com.hepipat.bookish.databinding.FragmentReturnBinding
import com.hepipat.bookish.helper.fileprocessing.reduceFileImage
import com.hepipat.bookish.helper.fileprocessing.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ReturnFragment : BaseFragment<FragmentReturnBinding>() {

    private val viewModel: ReturnViewModel by viewModels()

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

        val returnRequestBody = ReturnRequestBody (currentDateandTime, 3)

        if (partFile != null) {
            viewModel.returnBook(partFile, returnRequestBody)
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
        TODO("Not yet implemented")
    }

    private fun openGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImg: Uri = data.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            binding.ivImg.load(selectedImg)

            getFile = myFile
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return, container, false)
    }

    companion object {
        private const val FILE_TYPE = "image/jpeg"
        private const val FILE_PARAM = "avatar"
    }
}