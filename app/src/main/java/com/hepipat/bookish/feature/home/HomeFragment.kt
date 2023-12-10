package com.hepipat.bookish.feature.home

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
import com.hepipat.bookish.databinding.FragmentHomeBinding
import com.hepipat.bookish.feature.mybooks.BookTitleAdapter
import com.hepipat.bookish.helper.error.ErrorCodeHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var adapter: BookTitleAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
    }

    override fun initRecyclerView() {
        super.initRecyclerView()
        adapter = BookTitleAdapter()
        adapter.itemClickListener = {
            val action =
                HomeFragmentDirections.actionHomeFragmentToDetailBookFragment(it)
            findNavController().navigate(action)
        }

        binding.apply {
            rvBooks.layoutManager = LinearLayoutManager(requireContext())
            rvBooks.adapter = adapter
        }
    }

    override fun initFetchData() {
        super.initFetchData()
        viewModel.getHomeBooks()
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.booksUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                when (it) {
                    is HomeBooksUiState.Success -> {
                        binding.pbBooksList.visibility = View.GONE
                        adapter.submitList(it.books)
                    }
                    is HomeBooksUiState.Empty -> {}
                    is HomeBooksUiState.Failed -> {}
                    is HomeBooksUiState.Error -> {
                        binding.pbBooksList.visibility = View.GONE
                        ErrorCodeHelper.getErrorMessage(requireContext(), it.exception)?.let { message ->
                            showToast(message)
                        }
                    }
                    is HomeBooksUiState.Loading -> {
                        binding.pbBooksList.visibility = View.VISIBLE
                    }
                }
            }
            .launchIn(lifecycleScope)
    }
}