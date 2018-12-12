package games.game2048

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestGame2048Helper {

    @Test
    fun `remove nulls from list`() {
        testMerge(listOf(null, "a"), listOf("a"))
    }

    @Test
    fun `two matching elements in the list are merged and the duplicate removed`() {
        testMerge(listOf("b", "b"), listOf("bb"))
    }

    @Test
    fun testSample1() = testMerge(listOf("a", "a", "b"), listOf("aa", "b"))

    @Test
    fun testSample2() = testMerge(listOf("a", null), listOf("a"))


    @Test
    fun testSample3() = testMerge(listOf("b", null, "a", "a"), listOf("b", "aa"))

    @Test
    fun testSample4() = testMerge(listOf("a", "a", null, "a"), listOf("aa", "a"))

    @Test
    fun testSample5() = testMerge(listOf("a", null, "a", "a"), listOf("aa", "a"))

    private fun testMerge(input: List<String?>, expected: List<String?>) {
        val result = input.moveAndMergeEqual { it.repeat(2) }
        assertEquals(expected, result, "Wrong result for $input.moveAndMergeEqual()")
    }
}