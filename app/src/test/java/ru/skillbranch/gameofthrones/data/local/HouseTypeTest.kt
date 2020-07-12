package ru.skillbranch.gameofthrones.data.local

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import ru.skillbranch.gameofthrones.data.local.entities.HouseType


class HouseTypeTest {

    @Rule
    @JvmField
    var exception = ExpectedException.none()

    @Test
    fun test_throwsIfUndefinedHouse() {
        exception.expect(java.lang.IllegalStateException::class.java)
        HouseType.fromString("nope")
    }

    @Test
    fun test_correctStringGivesCorrectHouse() {
        assertEquals(HouseType.STARK, HouseType.fromString("Stark"))
        assertEquals(HouseType.MARTELL, HouseType.fromString("Martell"))
    }

    @Test
    fun test_equals() {
        assertEquals("Martell", HouseType.fromString("Martell").toString())
    }
}