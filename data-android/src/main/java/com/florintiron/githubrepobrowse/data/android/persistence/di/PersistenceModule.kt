package com.florintiron.githubrepobrowse.data.android.persistence.di

import android.content.Context
import androidx.room.Room
import com.florintiron.githubrepobrowse.data.android.persistence.RepoDatabase
import com.florintiron.githubrepobrowse.data.android.persistence.RepoDatabase.Companion.DATABASE_NAME
import com.florintiron.githubrepobrowse.data.android.persistence.datasource.LocalRepoDataSourceImpl
import com.florintiron.githubrepobrowse.data.android.repository.datasource.LocalRepoDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Singleton
    @Provides
    fun provideRoomDb(@ApplicationContext appContext: Context): RepoDatabase =
        Room.databaseBuilder(
            appContext,
            RepoDatabase::class.java, DATABASE_NAME
        )
            .build()

    @Singleton
    @Provides
    fun provideLocalRepoDataSource(repoDatabase: RepoDatabase): LocalRepoDataSource =
        LocalRepoDataSourceImpl(repoDatabase)
}