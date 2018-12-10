package com.example.hicabbie.di.module

import com.example.hicabbie.service.ServiceCar
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeServiceCar():ServiceCar
}