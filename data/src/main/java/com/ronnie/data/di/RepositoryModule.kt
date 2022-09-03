package com.ronnie.data.di

import com.ronnie.data.repository.NewsRepositoryImpl
import com.ronnie.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("Unused")
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun providesRepository(repositoryImpl: NewsRepositoryImpl): NewsRepository
}