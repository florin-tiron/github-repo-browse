package com.florintiron.githubrepobrowse.data.android.repository

import com.florintiron.githubrepobrowse.data.android.mapToDomain
import com.florintiron.githubrepobrowse.data.android.mapToEntity
import com.florintiron.githubrepobrowse.data.android.network.NetworkError
import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import com.florintiron.githubrepobrowse.data.android.repository.datasource.LocalRepoDataSource
import com.florintiron.githubrepobrowse.data.android.repository.datasource.RemoteRepoDataSource
import com.florintiron.githubrepobrowse.data.android.toNetworkValue
import com.florintiron.githubrepobrowse.data.android.toPersistenceValue
import com.florintiron.githubrepobrowse.domain.base.Result
import com.florintiron.githubrepobrowse.domain.repository.GithubRepoRepository
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class GithubRepoSearchRepositoryImpl(
    private val remoteRepoDataSource: RemoteRepoDataSource,
    private val localRepoDataSource: LocalRepoDataSource,
    private val coroutineContext: CoroutineContext
) : GithubRepoRepository {

    override suspend fun getRepositories(
        queryText: String,
        sort: Sort,
        order: Order
    ): Result<List<GithubRepoData>> {
        return withContext(coroutineContext) {
            try {
                val result = remoteRepoDataSource.getGithubRepoList(
                    queryText,
                    sort.toNetworkValue(),
                    order.toNetworkValue(),
                    null,
                    null
                )
                if (result.isSuccessful) {
                    result.body()?.items?.also { githubRepoList ->
                        saveToLocal(githubRepoList)
                    }.let { githubRepoList ->
                        Result.SuccessData(githubRepoList?.map { it.mapToDomain() } ?: emptyList())
                    }
                } else {
                    Result.FailureData(NetworkError.ServerError(result.code(), result.message()))
                }
            } catch (exception: Exception) {
                if (exception is IOException) {
                    getLocalData(queryText, sort, order)
                } else {
                    Result.FailureData(exception)
                }
            }
        }
    }

    private suspend fun getLocalData(
        queryText: String,
        sort: Sort,
        order: Order
    ) = Result.SuccessData(
        localRepoDataSource.getGithubRepoList(
            queryText,
            sort.toPersistenceValue(),
            order.toPersistenceValue()
        ).map {
            it.mapToDomain()
        }
    )

    private suspend fun saveToLocal(githubRepoList: List<GithubRepo>) {
        localRepoDataSource.saveAll(githubRepoList.map {
            it.mapToEntity()
        })
    }
}
