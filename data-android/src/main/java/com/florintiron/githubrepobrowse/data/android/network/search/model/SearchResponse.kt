package com.florintiron.githubrepobrowse.data.android.network.search.model

data class SearchResponse<T>(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<T>?
)