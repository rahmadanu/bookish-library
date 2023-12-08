package com.hepipat.bookish.feature.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hepipat.bookish.R
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentBorrowBinding
import com.hepipat.bookish.databinding.FragmentScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BorrowFragment : BaseFragment<FragmentBorrowBinding>() {

    private val borrowArgs: BorrowFragmentArgs by navArgs()

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
            }
        }
    }
}