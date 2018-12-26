package com.example.hicabbie.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.example.hicabbie.data.db.AppDatabase
import com.example.hicabbie.data.db.DaoMessage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app.applicationContext,AppDatabase::class.java,"DB_NAME").build()
    }

    @Singleton
    @Provides
    fun provideMessageDao(db: AppDatabase): DaoMessage {
        return db.getMessageDao()
    }
}