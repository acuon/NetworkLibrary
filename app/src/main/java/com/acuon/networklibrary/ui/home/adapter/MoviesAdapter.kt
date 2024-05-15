package com.acuon.networklibrary.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acuon.networklibrary.databinding.ItemLayoutMoviesBinding
import com.acuon.networklibrary.domain.model.MovieItem
import com.acuon.networklibrary.utils.extensions.executeWithAction
import com.acuon.networklibrary.utils.extensions.inflater
import javax.inject.Inject

/**
 * Adapter for displaying a list of searched movies in a RecyclerView.
 * This adapter is responsible for binding movie data to ItemLayoutMoviesBinding.
 */
class MoviesAdapter @Inject constructor() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    /**
     * The list of searched movies to display.
     */
    var list: List<MovieItem?>? = null
        set(value) {
            field = value
            notifyItemRangeChanged(0, value?.size ?: 0)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLayoutMoviesBinding.inflate(parent.inflater(), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, list?.get(position))
    }

    /**
     * Listener for MovieItem click.
     */
    private var onMovieClick: ((Int, MovieItem?) -> Unit)? = null

    /**
     * Sets a listener to handle clicks on MovieItem.
     */
    fun setOnMovieClickListener(listener: (Int, MovieItem?) -> Unit) {
        onMovieClick = listener
    }

    /**
     * Sets a listener to handle clicks on Favorite icon.
     */
    inner class ViewHolder(private val binding: ItemLayoutMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, movie: MovieItem?) {
            binding.executeWithAction { item = movie }
            binding.root.setOnClickListener {
                onMovieClick?.invoke(position, movie)
            }
        }
    }
}