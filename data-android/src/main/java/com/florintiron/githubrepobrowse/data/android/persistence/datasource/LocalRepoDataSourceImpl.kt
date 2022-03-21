package com.florintiron.githubrepobrowse.data.android.persistence.datasource

import com.florintiron.githubrepobrowse.data.android.persistence.RepoDatabase
import com.florintiron.githubrepobrowse.data.android.persistence.model.RepoEntity
import com.florintiron.githubrepobrowse.data.android.repository.datasource.LocalRepoDataSource

class LocalRepoDataSourceImpl(private val database: RepoDatabase) : LocalRepoDataSource {

    override suspend fun saveAll(repoEntities : List<RepoEntity>) {
        return database.RepoDao().insertAll(repoEntities)
    }

    override suspend fun getGithubRepoList(query: String, sort: String, order: String): List<RepoEntity> {
        return database.RepoDao().getBySearchNameWithSorting(query, sort, order)
    }
}