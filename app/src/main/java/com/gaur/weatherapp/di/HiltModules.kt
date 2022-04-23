package com.gaur.weatherapp.di

import android.content.Context
import com.gaur.weatherapp.network.ApiService
import com.gaur.weatherapp.utils.AppSharedPref
import com.gaur.weatherapp.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@InstallIn(SingletonComponent::class)
@Module
object HiltModules {


    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder().baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): AppSharedPref {
        return AppSharedPref(context)
    }

}