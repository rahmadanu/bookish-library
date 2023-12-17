package com.hepipat.bookish.feature.returnbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hepipat.bookish.core.data.remote.request.ReturnRequestBody
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
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ReturnViewModel @Inject constructor(
    private val repository: BooksRepository,
) : ViewModel() {
    private val _returnUiState = MutableStateFlow<ReturnBookUiState>(ReturnBookUiState.Loading)
    val returnUiState = _returnUiState.asStateFlow()

    fun returnBook(partFile: MultipartBody.Part, returnBook: ReturnRequestBody) {
        viewModelScope.launch {
            returnBookState(partFile, returnBook, repository = repository)
                .collect { _returnUiState.value = it }
        }
    }
}

private fun returnBookState(
    partFile: MultipartBody.Part,
    returnBook: ReturnRequestBody,
    repository: BooksRepository,
): Flow<ReturnBookUiState> {
    return flow { emit(repository.returnBook(partFile, returnBook)) }
        .map { result ->
            when (result) {
                is Result.Success -> {
                    ReturnBookUiState.Success
                }

                is Result.Error -> {
                    ReturnBookUiState.Error(result.exception)
                }

                is Result.Loading -> {
                    ReturnBookUiState.Loading
                }
            }
        }
        .onStart { emit(ReturnBookUiState.Loading) }
}