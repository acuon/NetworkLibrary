package com.acuon.networklibrary.ui.movie_detail

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.acuon.networklibrary.R
import com.acuon.networklibrary.common.BaseFragment
import com.acuon.networklibrary.common.BundleKeys
import com.acuon.networklibrary.common.ResultOf
import com.acuon.networklibrary.databinding.FragmentMovieDetailBinding
import com.acuon.networklibrary.ui.movie_detail.viewmodel.MovieDetailViewModel
import com.acuon.networklibrary.utils.extensions.executeWithAction
import com.acuon.networklibrary.utils.extensions.gone
import com.acuon.networklibrary.utils.extensions.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {
    override fun getLayoutId() = R.layout.fragment_movie_detail

    private val viewModel: MovieDetailViewModel by activityViewModels()
    private val movieId by lazy { arguments?.getInt(BundleKeys.MOVIE_ID) }

    override fun setupViews() {
        viewModel.getMovieDetails(movieId)
        binding.apply {
            header.apply {
                ivLeftIcon.setImageResource(R.drawable.ic_back_arrow)
                ivLeftIcon.setOnClickListener(clickListener)
            }
            rgOptions.setOnCheckedChangeListener { _, id ->
                when (id) {
                    rbDescription.id -> handleOptions(false)
                    rbTrailer.id -> handleOptions(true)
                }
            }
            btnRetry.setOnClickListener(clickListener)
        }
    }

    private fun handleOptions(webView: Boolean) {
        binding.apply {
            if (webView) {
                webViewTrailer.show()
                tvLongDesc.gone()
                loadWebViewWithUrl()
            } else {
                webViewTrailer.gone()
                tvLongDesc.show()
                webViewTrailer.stopLoading()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebViewWithUrl() {
        binding.apply {
            webViewTrailer.settings.apply {
                javaScriptEnabled = true
                builtInZoomControls = true
                displayZoomControls = false
                setSupportZoom(true)
            }

            webViewTrailer.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url!!)
                    return false
                }
            }
            viewModel.trailerUrl.get()?.let { webViewTrailer.loadUrl(it) }
        }
    }

    override fun bindViewModel() {
        viewModel.movieItem bindTo {
            if (it is ResultOf.Success) {
                Log.d("MovieDetail", it.data.toString())
                viewModel.movie.set(it.data)
            }
            binding.executeWithAction {
                movieUiState = MovieDetailUIState(it)
            }
        }
    }

    override fun onViewClicked(view: View?) {
        when (view) {
            binding.header.ivLeftIcon -> {
                findNavController().popBackStack()
            }

            binding.btnRetry -> {
                viewModel.getMovieDetails(imdbId = movieId)
            }
        }
    }
}