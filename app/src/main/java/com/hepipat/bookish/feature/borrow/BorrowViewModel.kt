package com.hepipat.bookish.feature.borrow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hepipat.bookish.core.data.remote.request.BorrowRequestBody
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
class BorrowViewModel @Inject constructor(
    private val repository: BooksRepository,
) : ViewModel() {
    private val _borrowUiState = MutableStateFlow<BorrowBookUiState>(BorrowBookUiState.Loading)
    val borrowUiState = _borrowUiState.asStateFlow()

    fun borrowBook(borrow: BorrowRequestBody) {
        viewModelScope.launch {
            borrowBookState(borrow, repository = repository)
                .collect { _borrowUiState.value = it }
        }
    }
}

private fun borrowBookState(
    borrow: BorrowRequestBody,
    repository: BooksRepository,
): Flow<BorrowBookUiState> {
    return flow { emit(repository.borrowBook(borrow)) }
        .map { result ->
            when (result) {
                is Result.Success -> {
                    BorrowBookUiState.Success
                }

                is Result.Error -> {
                    BorrowBookUiState.Error(result.exception)
                }

                is Result.Loading -> {
                    BorrowBookUiState.Loading
                }
            }
        }
        .onStart { emit(BorrowBookUiState.Loading) }
}