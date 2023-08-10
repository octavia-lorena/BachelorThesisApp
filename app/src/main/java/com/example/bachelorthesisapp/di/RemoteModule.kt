package com.example.bachelorthesisapp.di

import com.example.bachelorthesisapp.data.datasource.BusinessRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.ClientRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.EventRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.PostRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.RequestRemoteDataSourceImpl
import com.example.bachelorthesisapp.datasource.BusinessRemoteDataSource
import com.example.bachelorthesisapp.datasource.ClientRemoteDataSource
import com.example.bachelorthesisapp.datasource.EventRemoteDataSource
import com.example.bachelorthesisapp.datasource.PostRemoteDataSource
import com.example.bachelorthesisapp.datasource.RequestRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    @Binds
    @Singleton
    abstract fun bindBusinessRemoteDataSource(remoteDataSourceImpl: BusinessRemoteDataSourceImpl): BusinessRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindPostRemoteDataSource(remoteDataSourceImpl: PostRemoteDataSourceImpl): PostRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindEventRemoteDataSource(remoteDataSourceImpl: EventRemoteDataSourceImpl): EventRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRequestRemoteDataSource(remoteDataSourceImpl: RequestRemoteDataSourceImpl): RequestRemoteDataSource
    @Binds
    @Singleton
    abstract fun bindClientRemoteDataSource(remoteDataSourceImpl: ClientRemoteDataSourceImpl): ClientRemoteDataSource


}