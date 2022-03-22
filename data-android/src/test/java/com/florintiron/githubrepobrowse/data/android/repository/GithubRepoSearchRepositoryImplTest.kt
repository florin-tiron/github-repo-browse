package com.florintiron.githubrepobrowse.data.android.repository

import com.florintiron.githubrepobrowse.data.android.network.NetworkError
import com.florintiron.githubrepobrowse.data.android.network.search.model.SearchResponse
import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import com.florintiron.githubrepobrowse.data.android.network.shared.model.Owner
import com.florintiron.githubrepobrowse.data.android.persistence.model.OwnerEntity
import com.florintiron.githubrepobrowse.data.android.persistence.model.RepoEntity
import com.florintiron.githubrepobrowse.data.android.repository.datasource.LocalRepoDataSource
import com.florintiron.githubrepobrowse.data.android.repository.datasource.RemoteRepoDataSource
import com.florintiron.githubrepobrowse.data.android.toNetworkValue
import com.florintiron.githubrepobrowse.data.android.toPersistenceValue
import com.florintiron.githubrepobrowse.domain.base.Result
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException


@ExperimentalCoroutinesApi
class GithubRepoSearchRepositoryImplTest {

    private val remoteRepoDataSource = mockk<RemoteRepoDataSource>()
    private val localDataSource = mockk<LocalRepoDataSource>(relaxed = true)
    private val coroutineContext = Dispatchers.Unconfined

    private lateinit var sut: GithubRepoSearchRepositoryImpl

    @Before
    fun setUp() {
        sut = GithubRepoSearchRepositoryImpl(
            coroutineContext = coroutineContext,
            remoteRepoDataSource = remoteRepoDataSource,
            localRepoDataSource = localDataSource
        )
    }

    @Test
    fun `getRepositories when remote response received with success return success data`() =
        runTest {
            coEvery {
                remoteRepoDataSource.getGithubRepoList(
                    QUERY_TEXT,
                    SORT.toNetworkValue(),
                    ORDER.toNetworkValue()
                )
            } returns Response.success(
                SearchResponse(
                    total_count = 2,
                    incomplete_results = false,
                    items = REMOTE_REPO_LIST
                )
            )

            val result = sut.getRepositories(QUERY_TEXT, SORT, ORDER)

            assertEquals(Result.SuccessData(DOMAIN_REPO_LIST), result)
        }

    @Test
    fun `getRepositories when remote response received with error return error data`() =
        runTest {
            coEvery {
                remoteRepoDataSource.getGithubRepoList(
                    QUERY_TEXT,
                    SORT.toNetworkValue(),
                    ORDER.toNetworkValue()
                )
            } returns Response.error(404, "{ error:404 }".toResponseBody())


            val result = sut.getRepositories(QUERY_TEXT, SORT, ORDER)

            assertEquals(
                Result.FailureData(NetworkError.ServerError(404)),
                result
            )
        }

    @Test
    fun `getRepositories when remote response received with success save to local`() =
        runTest {
            coEvery {
                remoteRepoDataSource.getGithubRepoList(
                    QUERY_TEXT,
                    SORT.toNetworkValue(),
                    ORDER.toNetworkValue()
                )
            } returns Response.success(
                SearchResponse(
                    total_count = 2,
                    incomplete_results = false,
                    items = REMOTE_REPO_LIST
                )
            )

            sut.getRepositories(QUERY_TEXT, SORT, ORDER)

            coVerify {
                localDataSource.saveAll(ENTITY_REPO_LIST)
            }
        }

    @Test
    fun `getRepositories when remote throws IO Exception return success from local`() =
        runTest {
            coEvery {
                remoteRepoDataSource.getGithubRepoList(
                    QUERY_TEXT,
                    SORT.toNetworkValue(),
                    ORDER.toNetworkValue()
                )
            } throws IO_EXCEPTION
            coEvery {
                localDataSource.getGithubRepoList(
                    QUERY_TEXT,
                    SORT.toPersistenceValue(),
                    ORDER.toPersistenceValue()
                )
            } returns ENTITY_REPO_LIST

            val result = sut.getRepositories(QUERY_TEXT, SORT, ORDER)

            assertEquals(Result.SuccessData(DOMAIN_REPO_LIST), result)
        }


    companion object {
        private const val QUERY_TEXT = "kotlin"
        private val SORT = Sort.STARS
        private val ORDER = Order.DESCENDING
        private val REMOTE_OWNER_1 = Owner(id = 1, login = "owner1", avatar_url = "owner1_url")
        private val REMOTE_OWNER_2 = Owner(id = 2, login = "owner2", avatar_url = "owner2_url")
        private val REMOTE_REPO_LIST = arrayListOf(
            GithubRepo(
                id = 1,
                name = "repo1",
                owner = REMOTE_OWNER_1,
                language = "kotlin",
                stargazers_count = 1
            ),
            GithubRepo(
                id = 2,
                name = "repo2",
                owner = REMOTE_OWNER_2,
                language = "kotlin",
                stargazers_count = 2
            )
        )
        private val DOMAIN_REPO_LIST = arrayListOf(
            GithubRepoData("1", "repo1", "owner1", "owner1_url", 1, "kotlin"),
            GithubRepoData("2", "repo2", "owner2", "owner2_url", 2, "kotlin")
        )

        private val ENTITY_REPO_LIST = arrayListOf(
            RepoEntity(
                id = 1,
                name = "repo1",
                stars = 1,
                language = "kotlin",
                owner = OwnerEntity(ownerId = 1, name = "owner1", avatarUrl = "owner1_url")
            ),
            RepoEntity(
                id = 2,
                name = "repo2",
                stars = 2,
                language = "kotlin",
                owner = OwnerEntity(ownerId = 2, name = "owner2", avatarUrl = "owner2_url")
            )
        )
        private val IO_EXCEPTION = IOException("error")
    }
}