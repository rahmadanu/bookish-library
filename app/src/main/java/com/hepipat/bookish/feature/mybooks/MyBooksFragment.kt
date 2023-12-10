package com.hepipat.bookish.feature.mybooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hepipat.bookish.core.base.fragment.BaseFragment
import com.hepipat.bookish.databinding.FragmentMyBooksBinding
import com.hepipat.bookish.helper.error.ErrorCodeHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyBooksFragment : BaseFragment<FragmentMyBooksBinding>() {

    private lateinit var adapter: BookTitleAdapter

    private val myBooksViewModel: MyBooksViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMyBooksBinding = FragmentMyBooksBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {

    }

    override fun initRecyclerView() {
        super.initRecyclerView()
        adapter = BookTitleAdapter()
        adapter.itemClickListener = {
            val action =
                MyBooksFragmentDirections.actionMyBooksFragmentToDetailBookFragment(it)
            findNavController().navigate(action)
        }

        binding.apply {
            rvMyBooks.layoutManager = LinearLayoutManager(requireContext())
            rvMyBooks.adapter = adapter
        }
    }

    override fun initFetchData() {
        super.initFetchData()
        myBooksViewModel.getMyBooks()
    }

    override fun initObserver() {
        super.initObserver()
        myBooksViewModel.booksUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                when (it) {
                    is MyBooksUiState.Success -> {
                        binding.pbMyBooksList.visibility = View.GONE
                        adapter.submitList(it.myBooks)
                    }
                    is MyBooksUiState.Empty -> {}
                    is MyBooksUiState.Failed -> {}
                    is MyBooksUiState.Error -> {
                        binding.pbMyBooksList.visibility = View.GONE
                        ErrorCodeHelper.getErrorMessage(requireContext(), it.exception)?.let { message ->
                            showToast(message)
                        }
                    }
                    is MyBooksUiState.Loading -> {
                        binding.pbMyBooksList.visibility = View.VISIBLE
                    }
                }
            }
            .launchIn(lifecycleScope)
    }
}