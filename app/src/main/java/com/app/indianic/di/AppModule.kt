package com.app.indianic.di

import com.app.indianic.BuildConfig
import com.app.indianic.api.ApiService
import com.app.indianic.utils.Configuration.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OtherInterceptorOkHttpClient

    @Provides
    @OtherInterceptorOkHttpClient
    fun providesLoggingInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @OtherInterceptorOkHttpClient
    fun providesRetrofitForPreLogin(): Retrofit {
        return Retrofit.Builder()
            .client(providesLoggingInterceptor())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @AuthInterceptorOkHttpClient
    fun providesRetrofitForPostLogin(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun providesOnApi(@OtherInterceptorOkHttpClient retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}