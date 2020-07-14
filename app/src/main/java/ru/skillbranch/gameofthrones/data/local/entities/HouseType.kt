package ru.skillbranch.gameofthrones.data.local.entities

import androidx.annotation.DrawableRes
import ru.skillbranch.gameofthrones.R

enum class HouseType(
    val title: String,
    @DrawableRes
    val icon: Int
) {
    STARK("Stark", R.drawable.stark_icon),
    LANNISTER("Lannister", R.drawable.lanister_icon),
    TARGARYEN("Targaryen", R.drawable.targaryen_icon),
    BARATHEON("Baratheon", R.drawable.baratheon_icon),
    GREYJOY("Greyjoy", R.drawable.greyjoy_icon),
    MARTELL("Martell", R.drawable.martel_icon),
    TYRELL("Tyrell", R.drawable.tyrel_icon);

    override fun toString(): String {
        return title
    }

    companion object {
        fun fromString(title: String): HouseType {
            val found = HouseType.values().find { it.title.equals(title) }
            return found ?: throw IllegalStateException("No such house: $title")
        }
    }

}