package com.hepipat.bookish.feature.scan

import com.hepipat.bookish.core.domain.model.BooksUi

sealed interface ScanBooksUiState {
    data class Scanned(val booksUi: BooksUi) : ScanBooksUiState
    data class Failed(val message: String) : ScanBooksUiState
    data class Error(val exception: Throwable) : ScanBooksUiState
    object NotFound : ScanBooksUiState
    object Loading : ScanBooksUiState
}