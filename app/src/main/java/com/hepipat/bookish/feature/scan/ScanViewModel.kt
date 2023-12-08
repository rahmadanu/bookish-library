package com.hepipat.bookish.feature.scan

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
class ScanViewModel @Inject constructor(
    private val repository: BooksRepository,
) : ViewModel() {
    private val _booksUiState = MutableStateFlow<ScanBooksUiState>(ScanBooksUiState.Loading)
    val booksUiState = _booksUiState.asStateFlow()

    fun scanBooks(isbnCode: String) {
        viewModelScope.launch {
            initScanBooksUiState(isbnCode = isbnCode, repository = repository)
                .collect { _booksUiState.value = it }
        }
    }
}

private fun initScanBooksUiState(
    isbnCode: String,
    repository: BooksRepository,
): Flow<ScanBooksUiState> {
    return flow { emit(repository.getBooksByIsbn(isbnCode)) }
        .map { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.id.isNotEmpty()) ScanBooksUiState.Scanned(result.data)
                    else ScanBooksUiState.NotFound
                }

                is Result.Error -> {
                    ScanBooksUiState.Error(result.exception)
                }

                is Result.Loading -> {
                    ScanBooksUiState.Loading
                }
            }
        }
        .onStart { emit(ScanBooksUiState.Loading) }
}