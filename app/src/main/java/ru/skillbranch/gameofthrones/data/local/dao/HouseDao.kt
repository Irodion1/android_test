package ru.skillbranch.gameofthrones.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.skillbranch.gameofthrones.data.local.entities.House

@Dao
interface HouseDao : BaseDao<House> {
    @Query(
        """
            SELECT COUNT(*) FROM house
        """
    )
    suspend fun recordsCount(): Int

    @Transaction
    fun upsert(list: List<House>) {
        insert(list)
            .mapIndexed { index, l -> if (l == -1L) list[index] else null }
            .filterNotNull()
            .also { if (it.isNotEmpty()) update(it) }
    }
}