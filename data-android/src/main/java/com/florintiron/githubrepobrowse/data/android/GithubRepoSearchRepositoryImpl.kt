package com.florintiron.githubrepobrowse.data.android

import com.florintiron.githubrepobrowse.data.android.network.NetworkError
import com.florintiron.githubrepobrowse.data.android.network.mapToDomain
import com.florintiron.githubrepobrowse.data.android.network.search.datasource.RemoteSearchDataSource
import com.florintiron.githubrepobrowse.data.android.network.toRemoteValue
import com.florintiron.githubrepobrowse.domain.base.Result
import com.florintiron.githubrepobrowse.domain.repository.GithubRepoRepository
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class GithubRepoSearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val coroutineContext: CoroutineContext
) :
    GithubRepoRepository {

    override suspend fun getRepositories(
        queryText: String,
        sort: Sort,
        order: Order
    ): Result<List<GithubRepoData>> {
        return withContext(coroutineContext) {
            try {
                val result = remoteSearchDataSource.getGithubRepoList(
                    queryText,
                    sort.toRemoteValue(),
                    order.toRemoteValue(),
                    null,
                    null
                )
                if (result.isSuccessful) {
                    Result.SuccessData(result.body()?.items?.map { it.mapToDomain() }
                        ?: emptyList())
                } else {
                    Result.FailureData(NetworkError.ServerError(result.code(), result.message()))
                }
            } catch (exception: IOException) {
                Result.FailureData(exception)
            }
        }
    }
}
