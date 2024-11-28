package com.saswat10.rnmapp.di

import com.saswat10.network.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesKtorClient(): KtorClient{
//        val config = ...
        return KtorClient()
    }
}
