package games.game2048

import board.Cell
import board.GameBoard
import board.createGameBoard
import games.game2048.RandomGame2048Initializer.nextValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestGame2048Initializer {

    operator fun <T> GameBoard<T>.get(i: Int, j: Int) = get(getCell(i, j))
    operator fun <T> GameBoard<T>.set(i: Int, j: Int, value: T) = set(getCell(i, j), value)
    private fun Cell?.asString() = if (this != null) "($i, $j)" else ""

    @Test
    fun `game initializer returns null if the game board has no cells`() {
        val gameBoard = createGameBoard<Int?>(0)
        assertEquals(null, nextValue(gameBoard))
    }

    @Test
    fun `game initializer returns null if all cells on the game board already contain values`() {
        val gameBoard = createGameBoard<Int?>(2)
        gameBoard[1, 1] = 2
        gameBoard[1, 2] = 2
        gameBoard[2, 1] = 2
        gameBoard[2, 2] = 2
        assertEquals(null, nextValue(gameBoard))
    }

    @Test
    fun `game initializer is able to find a free cell`() {
        val gameBoard = createGameBoard<Int?>(2)
        gameBoard[1, 1] = 2
        gameBoard[1, 2] = 2
        gameBoard[2, 1] = 2
        assertEquals("(2, 2)", nextValue(gameBoard)?.first.asString())
        assertEquals(null, gameBoard[2, 2])
    }
}