package com.hepipat.bookish.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hepipat.bookish.core.data.repository.BooksRepository
import com.hepipat.bookish.helper.api.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BooksRepository,
) : ViewModel() {
    private val _booksUiState = MutableStateFlow<HomeBooksUiState>(HomeBooksUiState.Loading)
    val booksUiState = _booksUiState.asStateFlow()

    fun getHomeBooks() {
        viewModelScope.launch {
            initHomeBooksUiState(repository = repository)
                .collect { _booksUiState.value = it }
        }
    }
}

private fun initHomeBooksUiState(
    repository: BooksRepository,
): Flow<HomeBooksUiState> {
    return flow { emit(repository.getBooks()) }
        .map { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.isNotEmpty()) HomeBooksUiState.Success(result.data)
                    else HomeBooksUiState.Empty
                }

                is Result.Error -> {
                    HomeBooksUiState.Error(result.exception)
                }

                is Result.Loading -> {
                    HomeBooksUiState.Loading
                }
            }
        }
        .onStart { emit(HomeBooksUiState.Loading) }
}