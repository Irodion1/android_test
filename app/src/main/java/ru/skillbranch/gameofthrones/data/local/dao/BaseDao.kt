package ru.skillbranch.gameofthrones.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T : Any> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(o: List<T>): List<Long>

    @Update
    fun update(o: List<T>)
}