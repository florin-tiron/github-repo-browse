package com.florintiron.githubrepobrowse.data.android.di

import com.florintiron.githubrepobrowse.data.android.GithubRepoSearchRepositoryImpl
import com.florintiron.githubrepobrowse.data.android.network.search.datasource.RemoteSearchDataSource
import com.florintiron.githubrepobrowse.domain.repository.GithubRepoRepository
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
    fun provideGithubRepoSearchRepository(
        dataSource: RemoteSearchDataSource
    ): GithubRepoRepository =
        GithubRepoSearchRepositoryImpl(
            remoteSearchDataSource = dataSource,
            coroutineContext = Dispatchers.IO
        )
}