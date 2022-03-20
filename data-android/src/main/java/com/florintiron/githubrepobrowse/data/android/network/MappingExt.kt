package com.florintiron.githubrepobrowse.data.android.network

import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Order.ASCENDING
import com.florintiron.githubrepobrowse.domain.repository.model.Order.DESCENDING
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.repository.model.Sort.STARS
import com.florintiron.githubrepobrowse.domain.repository.model.Sort.FORKS
import com.florintiron.githubrepobrowse.domain.repository.model.Sort.HELP_WANTED
import com.florintiron.githubrepobrowse.domain.repository.model.Sort.RECENT_UPDATES
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData

fun Sort.toRemoteValue() = when (this) {
    STARS -> "stars"
    FORKS -> "forks"
    HELP_WANTED -> "help-wanted-issues"
    RECENT_UPDATES -> "updates"
}

fun Order.toRemoteValue() = when (this) {
    ASCENDING -> "asc"
    DESCENDING -> "dsc"
}

fun GithubRepo.mapToDomain() = GithubRepoData(
    id = this.id.toString(),
    name = this.name,

    ownerName = this.owner.login,
    ownerAvatarUrl = this.owner.avatar_url,
    starsCount = stargazers_count,
    language = language
)
