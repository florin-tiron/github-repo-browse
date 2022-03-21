package com.florintiron.githubrepobrowse.data.android.repository.datasource

import com.florintiron.githubrepobrowse.data.android.network.search.model.SearchResponse
import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import retrofit2.Response

interface RemoteRepoDataSource {

    suspend fun getGithubRepoList(
        query: String,
        sort: String?,
        sortOrder: String?,
        resultPerPage: Int?,
        pageNumber: Int? = null
    ): Response<SearchResponse<GithubRepo>>

}