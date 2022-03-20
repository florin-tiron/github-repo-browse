package com.florintiron.githubrepobrowse.domain.shared.model

data class GithubRepoData(
    val id: String,
    val name: String,
    val ownerName: String,
    val ownerAvatarUrl: String,
    val starsCount: Int,
    val language: String
)