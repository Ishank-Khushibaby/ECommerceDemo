package com.example.ecommercedemo.di

import com.example.ecommercedemo.data.repository.ProductsRepository
import com.example.ecommercedemo.data.repository.ProductsRepositoryImpl
import com.example.ecommercedemo.data.service.ECommerceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://119edbb1-1037-4fa8-bd73-f661b0654ba6.mock.pstmn.io/"

    @Provides
    @Singleton
    fun providesMockletService(): ECommerceService {
        val okHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(true)
        }.build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ECommerceService::class.java)
    }


    @Provides
    @Singleton
    fun provideProductRepository(
        eCommerceService: ECommerceService,
    ): ProductsRepository {
        return ProductsRepositoryImpl(eCommerceService)
    }
}