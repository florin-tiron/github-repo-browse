package com.florintiron.githubrepobrowse.data.android.network.search.datasource

import com.florintiron.githubrepobrowse.data.android.network.GithubServiceApi
import com.florintiron.githubrepobrowse.data.android.network.search.model.SearchResponse
import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import retrofit2.Response

class RemoteSearchDataSourceImpl(private val githubServiceApi: GithubServiceApi) :
    RemoteSearchDataSource {
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