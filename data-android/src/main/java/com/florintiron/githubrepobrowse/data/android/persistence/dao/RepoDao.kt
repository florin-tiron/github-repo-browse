package com.florintiron.githubrepobrowse.data.android.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.florintiron.githubrepobrowse.data.android.persistence.model.RepoEntity

@Dao
interface RepoDao {
    @Query("SELECT * FROM RepoEntity")
    suspend fun getAll(): List<RepoEntity>

    @Query("SELECT * FROM RepoEntity WHERE name LIKE '%' || :searchText || '%' order by :sort || :order")
    suspend fun getBySearchNameWithSorting(
        searchText: String,
        sort: String,
        order: String
    ): List<RepoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepoEntity>)

    @Delete
    suspend fun delete(repo: RepoEntity)
}
