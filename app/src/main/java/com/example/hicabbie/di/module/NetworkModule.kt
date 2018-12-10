package com.example.di.module

import com.example.hicabbie.data.Api.Api
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providerInterCepter(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    }

    @Provides
    @Singleton
    fun provideOkhttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)

    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl("https://api.github.com")
            .client(client.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}