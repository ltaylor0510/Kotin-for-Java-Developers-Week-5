package games.board

import board.Cell
import board.GameBoard
import board.createGameBoard
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestGameBoard {
    operator fun <T> GameBoard<T>.get(i: Int, j: Int) = get(getCell(i, j))
    operator fun <T> GameBoard<T>.set(i: Int, j: Int, value: T) = set(getCell(i, j), value)
    private fun Cell?.asString() = if (this != null) "($i, $j)" else ""

    @Test
    fun testGetAndSetElement() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        assertEquals('a', gameBoard[1, 1])
    }

    @Test
    fun testFilter() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        gameBoard[1, 2] = 'b'
        val cells = gameBoard.filter { it == 'a' }
        assertEquals(1, cells.size)
        val cell = cells.first()
        assertEquals(1, cell.i)
        assertEquals(1, cell.j)
    }

    @Test
    fun testAll() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        gameBoard[1, 2] = 'a'
        assertFalse(gameBoard.all { it == 'a' })
        gameBoard[2, 1] = 'a'
        gameBoard[2, 2] = 'a'
        assertTrue(gameBoard.all { it == 'a' })
    }

    @Test
    fun testAny() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        gameBoard[1, 2] = 'b'
        assertTrue(gameBoard.any { it in 'a'..'b' })
        assertTrue(gameBoard.any { it == null })
    }

    @Test
    fun `test that find locates a null cell`() {
        val gameBoard = createGameBoard<Int?>(2)
        gameBoard[1, 1] = 1
        gameBoard[1, 2] = 2
        gameBoard[2, 1] = 3
        assertEquals("(2, 2)", gameBoard.find { it == null }.asString())
    }
}