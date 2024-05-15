package com.acuon.networklibrary.domain.model

import com.acuon.networklibrary.utils.extensions.DateFormats
import com.acuon.networklibrary.utils.extensions.serverToPrettyDate
import com.acuon.networklibrary.utils.extensions.toMinutes

data class MovieItem(
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val genreIds: List<Int?>? = null,
    val id: Int? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null
) {

    val prettyDate: String
        get() = releaseDate.serverToPrettyDate(
            DateFormats.DATE_FORMAT,
            DateFormats.PRETTY_DATE_FORMAT2
        )

    val prettyDateFull: String
        get() = releaseDate.serverToPrettyDate(
            DateFormats.DATE_FORMAT,
            DateFormats.PRETTY_DATE_FORMAT
        )

    val lengthInMinutes: String
        get() = "${100}m"
}