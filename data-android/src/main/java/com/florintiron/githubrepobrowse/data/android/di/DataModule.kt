package com.florintiron.githubrepobrowse.data.android.di

import com.florintiron.githubrepobrowse.data.android.GithubRepoSearchRepositoryImpl
import com.florintiron.githubrepobrowse.data.android.network.search.datasource.RemoteSearchDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideGithubRepoSearchRepositoryImpl(
        dataSourceImpl: RemoteSearchDataSourceImpl
    ) =
        GithubRepoSearchRepositoryImpl(
            remoteSearchDataSource = dataSourceImpl,
            coroutineContext = Dispatchers.IO
        )
}