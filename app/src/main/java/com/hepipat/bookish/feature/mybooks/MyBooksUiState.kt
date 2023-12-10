package com.hepipat.bookish.feature.mybooks

import com.hepipat.bookish.core.domain.model.TitleBooksUi

sealed interface MyBooksUiState {
    data class Success(val myBooks: List<TitleBooksUi>) : MyBooksUiState
    data class Failed(val message: String) : MyBooksUiState
    data class Error(val exception: Throwable) : MyBooksUiState
    object Empty : MyBooksUiState
    object Loading : MyBooksUiState
}