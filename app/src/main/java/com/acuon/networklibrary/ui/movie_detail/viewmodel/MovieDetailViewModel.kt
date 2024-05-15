package com.acuon.networklibrary.ui.movie_detail.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.acuon.networklibrary.common.BaseViewModel
import com.acuon.networklibrary.common.ResultOf
import com.acuon.networklibrary.data.remote.dto.MovieDetailsDTO
import com.acuon.networklibrary.domain.model.MovieItem
import com.acuon.networklibrary.domain.repository.IHomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: IHomeRepository
) : BaseViewModel() {

    private val _movieItem = MutableStateFlow<ResultOf<MovieDetailsDTO?>>(ResultOf.Loading())
    val movieItem: StateFlow<ResultOf<MovieDetailsDTO?>>
        get() = _movieItem

    val trailerUrl = ObservableField<String>()
    val movie = ObservableField<MovieDetailsDTO?>()

    fun getMovieDetails(imdbId: Int?) {
        execute {
            _movieItem.value = ResultOf.Loading()
            repository.getMovieDetail(imdbId,
                {
                    movie.set(it)
                    trailerUrl.set(movie.get()?.trailerUrl)
                    _movieItem.value = ResultOf.Success(it)
                }, { message, error ->
                    _movieItem.value = ResultOf.Error(message ?: "something went wrong")
                })
        }
    }

}