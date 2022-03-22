package com.florintiron.githubrepobrowse.data.android.network

sealed class NetworkError : Exception() {
    data class ServerError(val errorCode: Int, override val message: String = "") : NetworkError()
    object NoConnectivity : NetworkError()
}