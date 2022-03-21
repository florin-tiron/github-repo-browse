package com.florintiron.githubrepobrowse.data.android.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.florintiron.githubrepobrowse.data.android.persistence.dao.RepoDao
import com.florintiron.githubrepobrowse.data.android.persistence.model.RepoEntity

@Database(entities = [RepoEntity::class], version = 1)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun RepoDao(): RepoDao

    companion object {
        const val DATABASE_NAME = "RepoDb"
    }
}