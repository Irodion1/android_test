package ru.skillbranch.gameofthrones.data.remote.res

import org.junit.Test

class HouseResTest {

    fun demo(name: String): String {
        val lastPos = name.split(" ")
            .indexOf("of")
        return name.split(" ")[lastPos - 1]

    }

    @Test
    fun testNamesHouse() {
        var name = "House Allyrion of Godsgrace"
        System.out.println("$name  ${demo(name)}")
    }

}