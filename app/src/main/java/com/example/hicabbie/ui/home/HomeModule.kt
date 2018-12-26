package com.example.hicabbie.ui.home

import com.example.di.ActivityScoped
import com.example.hicabbie.data.api.Api
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class HomeModule {

    @Binds
    abstract fun bindsHomeView(homeAct: MainActivity): HomeView

    @Module
    companion object {
        @Provides
        @ActivityScoped
        @JvmStatic
        fun provideHomeRepository(api: Api, view: HomeView): ILocationRepo {
            return LocationRepoIMPL(api, view)
        }
    }
}