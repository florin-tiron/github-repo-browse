package com.florintiron.githubrepobrowse.domain.repository

import com.florintiron.githubrepobrowse.domain.base.Result
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData

interface GithubRepoRepository {
    suspend fun getRepositories(
        queryText: String,
        sort: Sort,
        order: Order
    ): Result<List<GithubRepoData>>
}