package com.hepipat.bookish.core.di

import com.hepipat.bookish.core.data.remote.service.BaseServiceFactory
import com.hepipat.bookish.core.data.remote.service.BooksApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBooksApiService(): BooksApiService = BaseServiceFactory.getBooksApiService()
}