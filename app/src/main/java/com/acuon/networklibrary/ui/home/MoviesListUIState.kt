package com.acuon.networklibrary.ui.home

import android.content.Context
import com.acuon.networklibrary.R
import com.acuon.networklibrary.common.ResultOf
import com.acuon.networklibrary.domain.model.MovieItem

/**
 * UI State data class for representing Home screen (movies search)
 *
 * @property state The result of the operation, wrapped in a [ResultOf] object.
 */
data class MoviesListUIState(
    private val state: ResultOf<List<MovieItem?>?>
) {

    /**
     * Checks if the UI state represents loading.
     */
    fun isLoading() = state is ResultOf.Loading

    /**
     * Checks if the UI state represents success.
     */
    fun isSuccess() = state is ResultOf.Success

    /**
     * Checks if the UI state represents an error.
     */
    fun isError() = state is ResultOf.Error

    /**
     * Retrieves the error message if the UI state represents an error, or a default message otherwise.
     *
     * @param context The context used to access string resources.
     * @return The error message or a default message.
     */
    fun getErrorMessage(context: Context) = if (state is ResultOf.Error) {
        state.message ?: context.getString(R.string.something_went_wrong)
    } else ""

    /**
     * Checks if the UI state represents an empty result.
     */
    fun isEmpty() = state is ResultOf.Success && state.data.isNullOrEmpty()

    /**
     * Retrieves the data if the UI state represents success, or null otherwise.
     */
    fun getData() = if (state is ResultOf.Success) state.data else null

}