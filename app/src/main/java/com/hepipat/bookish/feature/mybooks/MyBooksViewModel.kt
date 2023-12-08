package com.hepipat.bookish.feature.mybooks

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
class MyBooksViewModel @Inject constructor(
    private val repository: BooksRepository,
) : ViewModel() {
    private val _booksUiState = MutableStateFlow<MyBooksUiState>(MyBooksUiState.Loading)
    val booksUiState = _booksUiState.asStateFlow()

    fun getMyBooks() {
        viewModelScope.launch {
            initMyBooksUiState(repository = repository)
                .collect { _booksUiState.value = it }
        }
    }
}

private fun initMyBooksUiState(
    repository: BooksRepository,
): Flow<MyBooksUiState> {
    return flow { emit(repository.getMyBooks()) }
        .map { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.isNotEmpty()) MyBooksUiState.Success(result.data)
                    else MyBooksUiState.NotFound
                }

                is Result.Error -> {
                    MyBooksUiState.Error(result.exception)
                }

                is Result.Loading -> {
                    MyBooksUiState.Loading
                }
            }
        }
        .onStart { emit(MyBooksUiState.Loading) }
}