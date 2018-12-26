package com.example.di

import android.app.Application
import com.example.di.module.ActivityBuilderModule
import com.example.di.module.AppModule
import com.example.di.module.NetworkModule
import com.example.hicabbie.MvpApplication
import com.example.hicabbie.di.module.DbModule
import com.example.hicabbie.di.module.ServiceBuilderModule
import com.example.hicabbie.di.module.ViewModelModule

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class, AppModule::class,
        NetworkModule::class, ServiceBuilderModule::class, DbModule::class,ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    override fun inject(instance: DaggerApplication)
    fun inject(mvpApplication: MvpApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindApplication(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }

}