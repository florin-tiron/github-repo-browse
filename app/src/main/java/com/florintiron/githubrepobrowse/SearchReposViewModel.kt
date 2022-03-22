package com.florintiron.githubrepobrowse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.florintiron.githubrepobrowse.domain.base.Result
import com.florintiron.githubrepobrowse.domain.search.GetMostStarredRepoUseCase
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_SEARCH_CHAR = 3
private const val DEBOUNCE_TIME_MILLIS = 300L

@HiltViewModel
internal class SearchReposViewModel @Inject constructor(
    private val getMostStarredRepoUseCase: GetMostStarredRepoUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow(SearchRepoViewState.Empty)
    val uiState: StateFlow<SearchRepoViewState> = _uiState

    init {
        initSearchQueryFlow()
    }

    fun onSearchForRepository(query: String) {
        if (query.length < MIN_SEARCH_CHAR) {
            return
        }
        searchQuery.value = query
    }

    private fun initSearchQueryFlow() {
        viewModelScope.launch {
            searchQuery.debounce(DEBOUNCE_TIME_MILLIS).onEach {
                searchQuery.value = it
                showLoadingState()
                performSearch(query = it)
            }.collect()
        }
    }

    private suspend fun performSearch(query: String) {
        val result = getMostStarredRepoUseCase.invoke(query)
        _uiState.value = when (result) {
            is Result.SuccessData -> SearchRepoViewState(
                query = query,
                searchResults = result.data,
                refreshing = false
            )
            is Result.FailureData -> SearchRepoViewState(
                query = query,
                refreshing = false,
                errorMessage = result.exception.message
            )
        }
    }

    private fun showLoadingState() {
        _uiState.value = _uiState.value.copy(refreshing = true)
    }
}

internal data class SearchRepoViewState(
    val query: String = "",
    val searchResults: List<GithubRepoData> = emptyList(),
    val refreshing: Boolean = false,
    val errorMessage: String? = null
) {
    companion object {
        val Empty = SearchRepoViewState()
    }
}