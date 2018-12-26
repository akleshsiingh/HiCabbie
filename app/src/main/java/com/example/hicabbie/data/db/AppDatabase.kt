package com.example.hicabbie.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.hicabbie.data.db.entity.EMessage

@Database(entities = [EMessage::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMessageDao(): DaoMessage

}