package com.hepipat.bookish.feature.mybooks

import com.hepipat.bookish.core.domain.model.MyBooksUi

sealed interface MyBooksUiState {
    data class Success(val myBooks: List<MyBooksUi>) : MyBooksUiState
    data class Failed(val message: String) : MyBooksUiState
    data class Error(val exception: Throwable) : MyBooksUiState
    object NotFound : MyBooksUiState
    object Loading : MyBooksUiState
}