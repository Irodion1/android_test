package ru.skillbranch.gameofthrones.data.local.entities

import androidx.room.*

@Entity(tableName = "character")
data class Character(
    @PrimaryKey
    val id: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String, //rel
    val mother: String, //rel
    val spouse: String,
    @ColumnInfo(name = "house_id")
    val houseId: String//rel
)

@DatabaseView(
    """
        SELECT house_id AS house, id, name, aliases, titles
        FROM character
        ORDER BY name ASC
    """
)
data class CharacterItem(
    val id: String,
    val house: String, //rel
    val name: String,
    val titles: List<String>,
    val aliases: List<String>
)

@DatabaseView(
    """
        SELECT 
            character.id,character.name, character.born, 
            character.died, character.name, character.titles, 
            character.aliases, character.mother, character.mother, 
            character.father, house.words, 
            mother.name AS m_name, mother.id AS m_id, mother.house_id as m_house,
            father.name AS f_name, father.id AS f_id, father.house_id as f_house
        FROM character
        LEFT JOIN character AS mother ON character.mother = mother.id
        LEFT JOIN character AS father ON character.father = father.id
        INNER JOIN house ON character.house_id = house_id
    """
)
data class CharacterFull(
    val id: String,
    val name: String,
    val words: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    @ColumnInfo(name = "house_id")
    val house: String, //rel
    @Embedded(prefix = "f_")
    val father: RelativeCharacter?,
    @Embedded(prefix = "m_")
    val mother: RelativeCharacter?
)


data class RelativeCharacter(
    val id: String,
    val name: String,
    val house: String //rel
)