package com.acuon.networklibrary.ui

import android.view.View
import com.acuon.networklibrary.R
import com.acuon.networklibrary.common.BaseActivity
import com.acuon.networklibrary.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutId() = R.layout.activity_main

    override fun setupViews() {
    }

    override fun onViewClicked(view: View?) {}

    override fun bindViewModel() {}
}