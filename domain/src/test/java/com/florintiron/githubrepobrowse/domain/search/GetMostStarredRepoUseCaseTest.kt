package com.florintiron.githubrepobrowse.domain.search

import com.florintiron.githubrepobrowse.domain.base.Result
import com.florintiron.githubrepobrowse.domain.repository.GithubRepoRepository
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class GetMostStarredRepoUseCaseTest {

    private val githubRepoRepository: GithubRepoRepository = mockk()
    private lateinit var sut: GetMostStarredRepoUseCase

    @Test
    fun `invoke when called return then repository is called`() = runTest {
        sut = GetMostStarredRepoUseCase(githubRepoRepository)
        coEvery {
            githubRepoRepository.getRepositories(
                QUERY_TEXT,
                SORT,
                ORDER
            )
        } returns Result.SuccessData(REPO_LIST)

        sut.invoke(QUERY_TEXT)

        coVerify {
            githubRepoRepository.getRepositories(QUERY_TEXT, SORT, ORDER)
        }
    }

    @Test
    fun `invoke when called return success data when repository returns success data`() = runTest {
        sut = GetMostStarredRepoUseCase(githubRepoRepository)
        coEvery {
            githubRepoRepository.getRepositories(
                QUERY_TEXT,
                SORT,
                ORDER
            )
        } returns Result.SuccessData(REPO_LIST)

        val result = sut.invoke(QUERY_TEXT)

        assertTrue(result == Result.SuccessData(REPO_LIST))
    }

    @Test
    fun `invoke when called return failure data when repository returns failure data`() = runTest {
        sut = GetMostStarredRepoUseCase(githubRepoRepository)
        coEvery {
            githubRepoRepository.getRepositories(
                QUERY_TEXT,
                SORT,
                ORDER
            )
        } returns Result.FailureData(EXCEPTION)

        val result = sut.invoke(QUERY_TEXT)

        assertTrue(result == Result.FailureData(EXCEPTION))
    }

    companion object {
        private const val QUERY_TEXT = "kotlin"
        private val SORT = Sort.STARS
        private val ORDER = Order.DESCENDING
        private val REPO_LIST = arrayListOf(
            GithubRepoData("1", "repo1", "owner1", "owner1_url", 1, "kotlin"),
            GithubRepoData("2", "repo2", "owner2", "owner2_url", 2, "kotlin")
        )
        private val EXCEPTION = Exception("error")
    }
}