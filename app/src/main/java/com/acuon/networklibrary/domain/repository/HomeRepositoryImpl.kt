package com.acuon.networklibrary.domain.repository

import android.content.Context
import com.acuon.networklibrary.BuildConfig
import com.acuon.networklibrary.common.ApiEndpoints
import com.acuon.networklibrary.data.remote.RequestHandler
import com.acuon.networklibrary.data.remote.dto.MovieDetailsDTO
import com.acuon.networklibrary.data.remote.dto.MovieItemDTO
import com.acuon.networklibrary.data.remote.dto.MovieListResponse
import com.acuon.simplenetworklibrary.enums.ContentType
import com.acuon.simplenetworklibrary.enums.RequestType
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val context: Context) : IHomeRepository {

    override suspend fun searchMovies(
        query: String,
        onResponse: (List<MovieItemDTO?>?) -> Unit,
        onError: (String?, Exception?) -> Unit
    ) {
        val requestType = RequestType.GET
        val contentType = ContentType.JSON

        val requestHandler = RequestHandler<MovieListResponse>(
            endPoint = ApiEndpoints.SEARCH,
            requestType = requestType,
            contentType = contentType,
            params = mapOf("query" to query, "api_key" to BuildConfig.API_KEY),
            onResponse = {
                onResponse.invoke(it?.results)
            },
            onError = { message, exception ->
                onError.invoke(message, exception)
            },
            clazz = MovieListResponse::class.java
        )

        requestHandler.execute()
    }

    override suspend fun getMovieDetail(
        imdbId: Int?,
        onResponse: (MovieDetailsDTO?) -> Unit,
        onError: (String?, Exception?) -> Unit
    ) {
        val requestHandler = RequestHandler<MovieDetailsDTO>(
            endPoint = ApiEndpoints.MOVIE_DETAIL.replace("{imdbId}", imdbId.toString()),
            requestType = RequestType.GET,
            contentType = ContentType.JSON,
            params = mapOf("append_to_response" to "videos", "api_key" to BuildConfig.API_KEY),
            onResponse = {
                onResponse.invoke(it)
            },
            onError = { message, exception ->
                onError.invoke(message, exception)
            },
            clazz = MovieDetailsDTO::class.java
        )

        requestHandler.execute()
    }
}

interface IHomeRepository {
    suspend fun searchMovies(
        query: String,
        onResponse: (List<MovieItemDTO?>?) -> Unit,
        onError: (String?, Exception?) -> Unit
    )

    suspend fun getMovieDetail(
        imdbId: Int?,
        onResponse: (MovieDetailsDTO?) -> Unit,
        onError: (String?, Exception?) -> Unit
    )
}
