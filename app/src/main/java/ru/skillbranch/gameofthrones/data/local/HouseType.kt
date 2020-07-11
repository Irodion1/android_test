package ru.skillbranch.gameofthrones.data.local

enum class HouseType(
    val title: String
) {
    STARK("Stark"),
    LANNISTER("Stark"),
    TARGARYEN("Targaryen"),
    BARATHEON("Baratheon"),
    GREYJOY("Greyjoy"),
    MARTELL("Martell"),
    TYRELL("Tyrell");

    companion object {
        fun fromString(title: String): HouseType {
            val found = HouseType.values().find { it.title.equals(title) }
            return found ?: throw IllegalStateException("No such house: $title")
        }
    }

}