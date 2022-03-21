package com.florintiron.githubrepobrowse.data.android.di

import com.florintiron.githubrepobrowse.data.android.repository.GithubRepoSearchRepositoryImpl
import com.florintiron.githubrepobrowse.data.android.repository.datasource.LocalRepoDataSource
import com.florintiron.githubrepobrowse.data.android.repository.datasource.RemoteRepoDataSource
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
        remoteDataSource: RemoteRepoDataSource,
        localRepoDataSource: LocalRepoDataSource,
    ): GithubRepoRepository =
        GithubRepoSearchRepositoryImpl(
            remoteRepoDataSource = remoteDataSource,
            localRepoDataSource = localRepoDataSource,
            coroutineContext = Dispatchers.IO
        )
}