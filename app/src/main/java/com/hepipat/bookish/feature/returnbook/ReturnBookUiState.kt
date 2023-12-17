package com.hepipat.bookish.feature.returnbook

sealed interface ReturnBookUiState {
    data class Error(val exception: Throwable) : ReturnBookUiState
    object Success : ReturnBookUiState
    object Loading : ReturnBookUiState
}