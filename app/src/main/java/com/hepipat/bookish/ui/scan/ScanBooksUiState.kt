package com.hepipat.bookish.ui.scan

import com.hepipat.bookish.core.domain.model.BooksUi

sealed interface ScanBooksUiState {
    data class Scanned(val booksUi: BooksUi) : ScanBooksUiState
    data class Failed(val message: String) : ScanBooksUiState
    data class Error(val message: String) : ScanBooksUiState
    object NotFound : ScanBooksUiState
    object Loading : ScanBooksUiState
}