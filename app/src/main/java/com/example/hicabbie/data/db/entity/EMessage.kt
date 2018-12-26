package com.example.hicabbie.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class EMessage(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var msg: String = "",
    var name: String = "",
    var mine: Boolean = false,
    var seen: Boolean = false
) {
    @Ignore
    constructor() : this(0)
}