package com.florintiron.githubrepobrowse.domain.search

import com.florintiron.githubrepobrowse.domain.base.BaseUseCase
import com.florintiron.githubrepobrowse.domain.base.Result
import com.florintiron.githubrepobrowse.domain.repository.GithubRepoRepository
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData

class GetMostStarredGithubRepoUseCase(private val githubRepoRepository: GithubRepoRepository) :
    BaseUseCase<String, List<GithubRepoData>> {

    override suspend fun invoke(param: String): Result<List<GithubRepoData>> {
        return githubRepoRepository.getRepositories(
            queryText = param,
            sort = Sort.STARS,
            order = Order.ASCENDING
        )
    }
}