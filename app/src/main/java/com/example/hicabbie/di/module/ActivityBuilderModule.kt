package com.example.di.module

import com.example.di.ActivityScoped
import com.example.hicabbie.ui.excel.ExcelActivity
import com.example.hicabbie.ui.home.HomeModule
import com.example.hicabbie.ui.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeGitHubActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeExcelActivity(): ExcelActivity
}