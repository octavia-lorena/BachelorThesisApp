package com.example.bachelorthesisapp.di

import com.example.bachelorthesisapp.data.businesses.remote.api.BusinessApi
import com.example.bachelorthesisapp.data.clients.remote.api.ClientApi
import com.example.bachelorthesisapp.data.events.remote.api.EventApi
import com.example.bachelorthesisapp.data.notifications.NotificationApi
import com.example.bachelorthesisapp.data.posts.remote.api.PostApi
import com.example.bachelorthesisapp.data.appointment_requests.remote.api.RequestApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.time.LocalDate
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL =  "http://10.0.2.2:3000"

    @Provides
    @Singleton
    fun provideEventApi(): EventApi {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDate::class.java, LocalDateAdapter().nonNull())
                .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            // .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
            .build()
            .create()
    }
    @Provides
    @Singleton
    fun provideBusinessApi(): BusinessApi {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDate::class.java, LocalDateAdapter().nonNull())
                .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            // .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDate::class.java, LocalDateAdapter().nonNull())
                .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            // .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideRequestApi(): RequestApi {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDate::class.java, LocalDateAdapter().nonNull())
                .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            // .addConverterFactory(GsonConverterFactory.create(Gson().newBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideNotificationsApi(): NotificationApi {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDate::class.java, LocalDateAdapter().nonNull())
                .build()
        return Retrofit.Builder()
            .baseUrl(NotificationApi.BASE_URL)
            //.addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideClientsApi(): ClientApi {
        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDate::class.java, LocalDateAdapter().nonNull())
                .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            //.addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}