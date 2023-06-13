package com.example.bachelorthesisapp.di

import com.example.bachelorthesisapp.data.remote.Api
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): Api {
        val moshi =
            Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
            .create()
    }
}