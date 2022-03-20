package com.florintiron.githubrepobrowse.data.android.network

import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import com.florintiron.githubrepobrowse.data.android.network.search.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubServiceApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") queryText: String,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("per_page") resultsPerPage: Int? = null,
        @Query("page") page: Int? = null
    ): Response<SearchResponse<GithubRepo>>
}