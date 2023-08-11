package com.example.bachelorthesisapp.di

import com.example.bachelorthesisapp.data.businesses.remote.BusinessRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.clients.remote.ClientRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.events.remote.EventRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.posts.remote.PostRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.appointment_requests.remote.RequestRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.businesses.remote.BusinessRemoteDataSource
import com.example.bachelorthesisapp.data.clients.remote.ClientRemoteDataSource
import com.example.bachelorthesisapp.data.events.remote.EventRemoteDataSource
import com.example.bachelorthesisapp.data.posts.remote.PostRemoteDataSource
import com.example.bachelorthesisapp.data.appointment_requests.remote.RequestRemoteDataSource
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