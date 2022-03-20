package com.florintiron.githubrepobrowse.data.android.network.di

import com.florintiron.githubrepobrowse.data.android.network.GithubServiceApi
import com.florintiron.githubrepobrowse.data.android.network.search.datasource.RemoteSearchDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://restcountries.eu/rest/v2/"
    private const val HEADER_ACCEPT_KEY = "Accept"
    private const val HEADER_ACCEPT_VALUE = "application/vnd.github.v3+json"

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    @Singleton
    @Provides
    fun providesHeaderInterceptor() = Interceptor { chain ->
        val request: Request =
            chain.request().newBuilder()
                .addHeader(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE).build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideRemoteSearchDataSource(apiService: GithubServiceApi) =
        RemoteSearchDataSourceImpl(apiService)
}