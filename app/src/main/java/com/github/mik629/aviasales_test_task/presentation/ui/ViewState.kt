package com.github.mik629.aviasales_test_task.presentation.ui


/**
 * Describes state of the view at any
 * point of time.
 */
sealed class ViewState<out S : Any?, out E : Any?> {

    data class Success<out S : Any?>(val result: S) : ViewState<S, Nothing>()

    object Loading : ViewState<Nothing, Nothing>()

    data class Error<out E : Any?>(val result: E) : ViewState<Nothing, E>()

    companion object {
        /**
         * Creates [ViewState] object with [Success] state and [data].
         */
        fun <S> success(data: S) = Success(data)

        /**
         * Creates [ViewState] object with [Loading] state to notify
         * the UI to showing loading.
         */
        fun loading() = Loading

        /**
         * Creates [ViewState] object with [Error] state and [error].
         */
        fun <E> error(error: E) = Error(error)
    }
}