package com.florintiron.githubrepobrowse.data.android.repository.datasource

import com.florintiron.githubrepobrowse.data.android.persistence.model.RepoEntity

interface LocalRepoDataSource {
    suspend fun getGithubRepoList(
        query: String,
        sort: String,
        order: String
    ): List<RepoEntity>

    suspend fun saveAll(repoEntities: List<RepoEntity>)
}