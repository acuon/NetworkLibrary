package com.acuon.networklibrary.ui.home.viewmodel

import com.acuon.networklibrary.common.BaseViewModel
import com.acuon.networklibrary.common.ResultOf
import com.acuon.networklibrary.data.remote.dto.toMovieItem
import com.acuon.networklibrary.domain.model.MovieItem
import com.acuon.networklibrary.domain.repository.IHomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IHomeRepository
) : BaseViewModel() {

    private val _moviesListState =
        MutableStateFlow<ResultOf<List<MovieItem?>?>>(ResultOf.Success(listOf()))
    val moviesListState: StateFlow<ResultOf<List<MovieItem?>?>>
        get() = _moviesListState

    private var searchJob: Job? = null

    fun searchMovies(query: String) {
        searchJob?.cancel()
        execute { delay(200) }
        searchJob = execute {
            _moviesListState.value = ResultOf.Loading()
            repository.searchMovies(query, {
                _moviesListState.value =
                    ResultOf.Success(it?.map { dto -> dto?.toMovieItem() ?: MovieItem() })
            }, { message, error ->
                _moviesListState.value = ResultOf.Error(message ?: "something went wrong")
            })
        }
    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

}