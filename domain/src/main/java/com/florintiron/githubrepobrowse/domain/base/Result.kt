package com.florintiron.githubrepobrowse.domain.base

import java.lang.Exception

sealed class Result<out T : Any> {
    data class SuccessData<out T : Any>(val data: T) : Result<T>()
    data class FailureData(val exception: Exception) : Result<Nothing>()
}