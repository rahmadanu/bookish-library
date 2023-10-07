package com.hepipat.bookish.core.di

import com.hepipat.bookish.core.data.remote.BooksRemoteDataSource
import com.hepipat.bookish.core.data.remote.BooksRemoteDataSourceImpl
import com.hepipat.bookish.core.data.repository.BooksRepository
import com.hepipat.bookish.core.data.repository.BooksRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideBooksRepository(booksRepositoryImpl: BooksRepositoryImpl): BooksRepository

    @Binds
    abstract fun provideBooksDataSource(booksRemoteDataSourceImpl: BooksRemoteDataSourceImpl): BooksRemoteDataSource
}