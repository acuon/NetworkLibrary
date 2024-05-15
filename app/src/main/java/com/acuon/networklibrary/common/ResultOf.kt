package com.acuon.networklibrary.common

/**
 * sealed class for representing the result of an operation(API call, database operations, etc)
 * - Success: indicating success status
 * - Error: indicating failure status
 * - Loading: indicating loading status
 *
 * @param T data type(Custom Data class or any data types)
 * @property data the data to access after success
 * @property message error message if the operation fails
 */
sealed class ResultOf<T>(val data: T? = null, val message: String? = null) {
    /**
     * @param data data associated with the successful operation
     */
    class Success<T>(data: T) : ResultOf<T>(data)

    /**
     * @param message error message associated with the failed operation
     * @param data optional data associated with the failed operation
     */
    class Error<T>(message: String, data: T? = null) : ResultOf<T>(data, message)

    /**
     * @param data optional data associated with the ongoing operation
     */
    class Loading<T>(data: T? = null) : ResultOf<T>(data)
}
