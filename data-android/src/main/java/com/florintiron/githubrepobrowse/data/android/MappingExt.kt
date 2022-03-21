package com.florintiron.githubrepobrowse.data.android

import com.florintiron.githubrepobrowse.data.android.network.shared.model.GithubRepo
import com.florintiron.githubrepobrowse.data.android.network.shared.model.Owner
import com.florintiron.githubrepobrowse.data.android.persistence.model.OwnerEntity
import com.florintiron.githubrepobrowse.data.android.persistence.model.RepoEntity
import com.florintiron.githubrepobrowse.domain.repository.model.Order
import com.florintiron.githubrepobrowse.domain.repository.model.Order.ASCENDING
import com.florintiron.githubrepobrowse.domain.repository.model.Order.DESCENDING
import com.florintiron.githubrepobrowse.domain.repository.model.Sort
import com.florintiron.githubrepobrowse.domain.repository.model.Sort.*
import com.florintiron.githubrepobrowse.domain.shared.model.GithubRepoData

fun Sort.toNetworkValue() = when (this) {
    STARS -> "stars"
    FORKS -> "forks"
    HELP_WANTED -> "help-wanted-issues"
    RECENT_UPDATES -> "updates"
}

fun Order.toNetworkValue() = when (this) {
    ASCENDING -> "asc"
    DESCENDING -> "dsc"
}

fun Sort.toPersistenceValue() = when (this) {
    STARS -> "star_count"
    FORKS -> "forks"
    HELP_WANTED -> "help-wanted-issues"
    RECENT_UPDATES -> "updates"
}

fun Order.toPersistenceValue() = when (this) {
    ASCENDING -> "asc"
    DESCENDING -> "dsc"
}

fun GithubRepo.mapToDomain() = GithubRepoData(
    id = this.id.toString(),
    name = this.name ?: "",
    ownerName = this.owner?.login ?: "",
    ownerAvatarUrl = this.owner?.avatar_url ?: "",
    starsCount = this.stargazers_count ?: 0,
    language = this.language ?: ""
)

fun GithubRepo.mapToEntity() = RepoEntity(
    id = this.id,
    name = this.name,
    language = this.language,
    stars = this.stargazers_count,
    owner = this.owner?.mapToEntity() ?: OwnerEntity(0, "Unknown", "")
)

fun Owner.mapToEntity() =
    OwnerEntity(ownerId = this.id, name = this.login ?: "", avatarUrl = this.avatar_url)

fun RepoEntity.mapToDomain() = GithubRepoData(
    id = this.id.toString(),
    name = this.name ?: "",
    ownerName = this.owner?.name?: "",
    ownerAvatarUrl = this.owner?.avatarUrl ?: "",
    starsCount = this.stars ?: 0,
    language = this.language ?: ""
)