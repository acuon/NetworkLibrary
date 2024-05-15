package com.acuon.networklibrary.ui.home

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.acuon.networklibrary.R
import com.acuon.networklibrary.common.BaseFragment
import com.acuon.networklibrary.common.BundleKeys
import com.acuon.networklibrary.common.ResultOf
import com.acuon.networklibrary.data.remote.dto.toMovieItem
import com.acuon.networklibrary.databinding.FragmentHomeBinding
import com.acuon.networklibrary.ui.home.adapter.MoviesAdapter
import com.acuon.networklibrary.ui.home.viewmodel.HomeViewModel
import com.acuon.networklibrary.utils.extensions.addDecoration
import com.acuon.networklibrary.utils.extensions.createGridDecorator
import com.acuon.networklibrary.utils.extensions.dp
import com.acuon.networklibrary.utils.extensions.executeWithAction
import com.acuon.networklibrary.utils.extensions.gridView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val viewModel: HomeViewModel by activityViewModels()
    override fun getLayoutId() = R.layout.fragment_home

    override fun setupViews() {
        binding.apply {
            rcvMovies.apply {
                gridView(context, 2)
                this.adapter = moviesAdapter
                addDecoration(createGridDecorator(8.dp, 8.dp, 24.dp, 24.dp, 2, true))
            }
        }
        setupListeners()
    }

    private var previousQuery: String? = null

    private fun setupListeners() {
        moviesAdapter.setOnMovieClickListener { _, movie ->
            findNavController().navigate(
                R.id.action_homeFragment_to_movieDetailFragment,
                bundleOf(BundleKeys.MOVIE_ID to movie?.id)
            )
        }
        binding.apply {
            etSearch.addTextChangedListener {
                val query = it.toString()
                if (query != previousQuery) {
                    runDelayed(100) {
                        viewModel.searchMovies(query)
                    }
                    previousQuery = query
                }
            }
            btnRetry.setOnClickListener(clickListener)
        }
    }

    override fun bindViewModel() {
        viewModel.moviesListState bindTo {
            if (it is ResultOf.Success) {
                moviesAdapter.list = it.data
            }
            binding.executeWithAction { uiState = MoviesListUIState(it) }
        }
    }

    override fun onViewClicked(view: View?) {
        when (view) {
            binding.btnRetry -> {
                viewModel.searchMovies(previousQuery.toString())
            }
        }
    }
}