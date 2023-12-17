package com.hepipat.bookish.feature.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hepipat.bookish.R
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentDetailBookBinding

class DetailBookFragment : BaseFragment<FragmentDetailBookBinding>() {

    private val bookArgs: DetailBookFragmentArgs by navArgs()

    private var borrowedId: String = ""
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDetailBookBinding = FragmentDetailBookBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        super.initView()
        binding.apply {
            bookArgs.booksUi.apply {
                Glide.with(requireContext())
                    .load(image)
                    .into(ivCover)

                tvTitle.text = title
                tvAuthor.text = author
                tvPublisher.text = publisher
                tvReleaseDate.text = releaseDate
                tvDescription.text = description

                if (borrowed) {
                    btnReturn.visibility = View.VISIBLE
                }

                borrowedId = borrowId
            }
        }
    }

    override fun initClickListener() {
        super.initClickListener()
        binding.btnReturn.setOnClickListener {
            val action =
                DetailBookFragmentDirections.actionDetailBookFragmentToReturnFragment(borrowedId)
            findNavController().navigate(action)
        }
    }
}