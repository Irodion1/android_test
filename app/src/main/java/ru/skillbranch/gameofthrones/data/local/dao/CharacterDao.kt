package ru.skillbranch.gameofthrones.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem

@Dao
interface CharacterDao : BaseDao<Character> {

    @Query(
        """
            SELECT * from CharacterItem
            WHERE house = :title
        """
    )
    fun findCharacters(title: String): LiveData<List<CharacterItem>>


    @Query(
        """
            SELECT * from CharacterItem
            where house =:title
        """
    )
    fun findCharacterList(title: String): List<CharacterItem>

    @Query(
        """
            SELECT * FROM CharacterFull
            WHERE id =:characterId
        """
    )
    fun findCharacter(characterId: String): LiveData<CharacterFull>

    @Query(
        """
            SELECT * FROM CharacterFull
            WHERE id =:characterId
        """
    )
    fun findCharacterFull(characterId: String): CharacterFull

    @Transaction
    fun upsert(list: List<Character>) {
        insert(list)
            .mapIndexed { index, l -> if (l == -1L) list[index] else null }
            .filterNotNull()
            .also { if (it.isNotEmpty()) update(it) }
    }

}