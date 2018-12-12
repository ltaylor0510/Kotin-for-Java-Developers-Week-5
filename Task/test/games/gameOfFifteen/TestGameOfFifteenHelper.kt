package games.gameOfFifteen

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestGameOfFifteenHelper {
    private fun testPermutation(permutation: List<Int>, isEven: Boolean) {
        assertEquals(isEven, isEven(permutation), "This permutation should be ${if (isEven) "even" else "odd"}: $permutation")
    }

    @Test
    fun testExample0() = testPermutation((1..15).toList(), isEven = true)

    @Test
    fun testExample1() = testPermutation(listOf(1, 2, 3, 4), isEven = true)

    @Test
    fun testExample2() = testPermutation(listOf(2, 1, 4, 3), isEven = true)

    @Test
    fun testExample3() = testPermutation(listOf(4, 3, 2, 1), isEven = true)

    @Test
    fun testExample5() = testPermutation(listOf(1, 2, 4, 3), isEven = false)

    @Test
    fun testExample6() = testPermutation(listOf(1, 4, 3, 2), isEven = false)
}