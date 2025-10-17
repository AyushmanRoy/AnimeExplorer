package com.ayushman.animeexplorer.presentation.states

/**
 * Sealed class representing different UI states
 * This helps manage loading, success, and error states consistently
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}

/**
 * Extension function to check if state is loading
 */
fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading

/**
 * Extension function to check if state is success
 */
fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success

/**
 * Extension function to check if state is error
 */
fun <T> UiState<T>.isError(): Boolean = this is UiState.Error

/**
 * Extension function to get data from success state
 */
fun <T> UiState<T>.getDataOrNull(): T? {
    return when (this) {
        is UiState.Success -> data
        else -> null
    }
}

/**
 * Extension function to get error message
 */
fun <T> UiState<T>.getErrorMessage(): String? {
    return when (this) {
        is UiState.Error -> message
        else -> null
    }
}