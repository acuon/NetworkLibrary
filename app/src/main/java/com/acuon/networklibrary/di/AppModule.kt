package com.acuon.networklibrary.di

import com.acuon.networklibrary.MoviesApp
import com.acuon.networklibrary.domain.repository.HomeRepositoryImpl
import com.acuon.networklibrary.domain.repository.IHomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHomeRepository(context: MoviesApp): IHomeRepository {
        return HomeRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideAppContext(): MoviesApp {
        return MoviesApp.getAppContext()
    }


}
