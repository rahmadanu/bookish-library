package com.hepipat.bookish.feature.home

import com.hepipat.bookish.core.domain.model.TitleBooksUi

sealed interface HomeBooksUiState {
    data class Success(val books: List<TitleBooksUi>) : HomeBooksUiState
    data class Failed(val message: String) : HomeBooksUiState
    data class Error(val exception: Throwable) : HomeBooksUiState
    object Empty : HomeBooksUiState
    object Loading : HomeBooksUiState
}