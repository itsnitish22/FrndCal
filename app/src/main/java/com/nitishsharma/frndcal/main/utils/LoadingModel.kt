package com.nitishsharma.frndcal.main.utils

data class LoadingModel(
    val loadingModel: LoadingState = LoadingState.LOADING,
    val error: Exception? = null,
    val isListEmpty: Boolean = false
)

enum class LoadingState {
    LOADING, COMPLETED, ERROR
}