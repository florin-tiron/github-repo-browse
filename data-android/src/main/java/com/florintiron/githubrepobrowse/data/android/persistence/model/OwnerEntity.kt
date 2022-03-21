package com.florintiron.githubrepobrowse.data.android.persistence.model

import androidx.room.ColumnInfo

data class OwnerEntity(
    @ColumnInfo(name = "owner_id") val ownerId: Int,
    @ColumnInfo(name = "userName") val name: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String? = null
)


