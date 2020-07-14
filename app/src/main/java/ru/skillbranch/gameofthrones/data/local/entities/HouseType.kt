package ru.skillbranch.gameofthrones.data.local.entities

import androidx.annotation.DrawableRes
import ru.skillbranch.gameofthrones.R

enum class HouseType(
    val title: String,
    @DrawableRes
    val icon: Int,

    @DrawableRes
    val arms: Int
) {
    STARK("Stark", R.drawable.stark_icon, R.drawable.stark_coast_of_arms),
    LANNISTER("Lannister", R.drawable.lanister_icon, R.drawable.lannister__coast_of_arms),
    TARGARYEN("Targaryen", R.drawable.targaryen_icon, R.drawable.targaryen_coast_of_arms),
    BARATHEON("Baratheon", R.drawable.baratheon_icon, R.drawable.baratheon_coast_of_arms),
    GREYJOY("Greyjoy", R.drawable.greyjoy_icon, R.drawable.greyjoy_coast_of_arms),
    MARTELL("Martell", R.drawable.martel_icon, R.drawable.martel_coast_of_arms),
    TYRELL("Tyrell", R.drawable.tyrel_icon, R.drawable.tyrel_coast_of_arms);

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