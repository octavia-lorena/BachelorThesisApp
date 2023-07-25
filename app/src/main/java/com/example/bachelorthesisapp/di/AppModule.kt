package com.example.bachelorthesisapp.di

import com.example.bachelorthesisapp.data.remote.Api
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.time.LocalDate
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): Api {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDate::class.java, LocalDateAdapter().nonNull())
                .build()
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            // .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
            .build()
            .create()
    }
}