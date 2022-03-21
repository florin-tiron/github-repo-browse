package com.florintiron.githubrepobrowse.data.android.network.search.datasource

import com.florintiron.githubrepobrowse.data.android.network.GithubServiceApi
import com.florintiron.githubrepobrowse.data.android.network.search.model.SearchResponse
import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import com.florintiron.githubrepobrowse.data.android.repository.datasource.RemoteRepoDataSource
import retrofit2.Response

class RemoteRepoDataSourceImpl(private val githubServiceApi: GithubServiceApi) :
    RemoteRepoDataSource {
    override suspend fun getGithubRepoList(
        query: String,
        sort: String?,
        sortOrder: String?,
        resultPerPage: Int?,
        pageNumber: Int?
    ): Response<SearchResponse<GithubRepo>> {
        return githubServiceApi.searchRepositories(
            queryText = query,
            sort = sort,
            order = sortOrder,
            resultsPerPage = resultPerPage,
            page = pageNumber
        )
    }
}