package com.florintiron.githubrepobrowse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData
import com.florintiron.githubrepobrowse.ui.elements.SearchTextField
import kotlinx.coroutines.flow.Flow


@Composable
fun SearchScreen(
    showRepoDetails: (repoId: String) -> Unit,
) {
    SearchScreenContent(
        viewModel = hiltViewModel(),
        showRepoDetails = showRepoDetails,
    )
}

@Composable
private fun SearchScreenContent(
    viewModel: SearchReposViewModel,
    showRepoDetails: (repoId: String) -> Unit,
) {
    val viewState by rememberFlowWithLifecycle(viewModel.uiState).collectAsState(initial = SearchRepoViewState.Empty)

    SearchScreenContent(
        state = viewState,
        showRepoDetails = showRepoDetails,
        onSearchQueryChanged = { viewModel.onSearchForRepository(it) },
    )
}

@Composable
private fun SearchScreenContent(
    state: SearchRepoViewState,
    showRepoDetails: (repoId: String) -> Unit,
    onSearchQueryChanged: (query: String) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    Modifier
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    var searchQuery by remember { mutableStateOf(TextFieldValue(state.query)) }
                    SearchTextField(
                        value = searchQuery,
                        onValueChange = { value ->
                            searchQuery = value
                            onSearchQueryChanged(value.text)
                        },
                        hint = "Search for repositories...",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
    ) { padding ->
        if (state.searchResults.isNotEmpty()) {
            ResultsList(
                results = state.searchResults,
                contentPadding = padding,
                onRepoClicked = { showRepoDetails(it.id) }
            )
        } else if (state.refreshing.not()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    color = Color.Gray,
                    text = "No results"
                )
            }

        }
        if (state.refreshing) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ResultsList(
    results: List<GithubRepoData>,
    onRepoClicked: (GithubRepoData) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(12.dp),
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(results) { data ->
            ResultRow(data, onRepoClicked, modifier)
        }
    }
}

@Composable
private fun ResultRow(
    repoData: GithubRepoData,
    onRepoClicked: (GithubRepoData) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .clickable {
            onRepoClicked.invoke(repoData)
        }
        .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = modifier
                    .padding(4.dp)
                    .weight(1.0f)
            ) {
                Text(
                    text = repoData.name,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Language: " + repoData.language)
                Text(text = "Stars: " + repoData.starsCount)
            }
            Column(modifier = modifier.padding(4.dp), horizontalAlignment = Alignment.End) {
                AsyncImage(
                    model = repoData.ownerAvatarUrl,
                    contentDescription = "${repoData.name} avatar",
                    modifier = Modifier.size(64.dp)
                )
                Text(text = repoData.ownerName)
            }
        }
    }
}

@Composable
fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = remember(flow, lifecycle) {
    flow.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState
    )
}


