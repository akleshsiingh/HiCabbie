package com.example.hicabbie.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.hicabbie.data.db.entity.EMessage
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
abstract class DaoMessage {

    @Query("SELECT * FROM EMessage")
    abstract fun getMessages(): Single<List<EMessage>>

    @Insert
    abstract fun insertMessage(msg: EMessage): Long

    @Query("SELECT * FROM EMessage WHERE seen=:seen")
    abstract fun getLatestmessage(seen: Boolean): Flowable<List<EMessage>>

    @Query("UPDATE EMessage SET  seen= 1 where id>=:from and id<=:to ")
    abstract fun updateMessageStatus(from: Long, to: Long)
}