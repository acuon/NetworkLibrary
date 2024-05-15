package com.acuon.networklibrary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

private var appContext: MoviesApp? = null

@HiltAndroidApp
class MoviesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        fun getAppContext(): MoviesApp {
            return appContext!!
        }
    }

}