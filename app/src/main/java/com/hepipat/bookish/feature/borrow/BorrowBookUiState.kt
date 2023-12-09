package com.hepipat.bookish.feature.borrow

sealed interface BorrowBookUiState {
    data class Error(val exception: Throwable) : BorrowBookUiState
    object Success : BorrowBookUiState
    object Loading : BorrowBookUiState
}