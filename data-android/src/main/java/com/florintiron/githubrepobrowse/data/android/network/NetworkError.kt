package com.florintiron.githubrepobrowse.data.android.network

sealed class NetworkError : Exception() {
    class ServerError(val errorCode: Int, message: String) : NetworkError()
    object NoConnectivity : NetworkError()
}