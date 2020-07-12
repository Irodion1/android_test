package ru.skillbranch.gameofthrones.data.remote.res

import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.HouseType

data class CharacterRes(
    val url: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String,
    val mother: String,
    val spouse: String,
    val allegiances: List<String> = listOf(),
    val books: List<String> = listOf(),
    val povBooks: List<Any> = listOf(),
    val tvSeries: List<String> = listOf(),
    val playedBy: List<String> = listOf()
) : IRes {
    lateinit var houseId: String
    override val id: String
        get() = url.lastSegment()
    val fatherId
        get() = father.lastSegment()
    val motherId
        get() = mother.lastSegment()


    fun toCharacter(house: String): Character {
        return Character(
            id,
            name,
            gender,
            culture,
            born,
            died,
            titles,
            aliases,
            father,
            mother,
            spouse,
            HouseType.fromString(house)
        )
    }
}

interface IRes {
    val id: String
    fun String.lastSegment(devider: String = "/"): String {
        return split(devider).last()
    }
}