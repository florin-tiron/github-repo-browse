package com.florintiron.githubrepobrowse.data.android.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepoEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "star_count") val stars: Int?,
    @ColumnInfo(name = "language") val language: String?,
    @Embedded val owner: OwnerEntity?
)