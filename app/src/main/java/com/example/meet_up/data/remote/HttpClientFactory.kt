package com.example.meet_up.data.remote

import at.bitfire.dav4jvm.BasicDigestAuthHandler
import com.example.meet_up.data.local.UserStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class HttpClientFactory {

    private val authHandler: BasicDigestAuthHandler
        get() = BasicDigestAuthHandler(
            domain = null, // Optional, to only authenticate against hosts with this domain.
            username = UserStorage.user.login,
            password = UserStorage.user.password
        )

    private lateinit var _client: OkHttpClient

    val client: OkHttpClient
        get() = _client

    fun create() {
        _client = OkHttpClient.Builder()
            .followRedirects(false)
            .authenticator(authHandler)
            .addNetworkInterceptor(authHandler)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }
}